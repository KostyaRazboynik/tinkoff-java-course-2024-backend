package edu.java.configuration.retry;

import edu.java.configuration.ApplicationConfig;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {

    private final ApplicationConfig applicationConfig;

    @Bean
    public Retry retry() {
        return switch (applicationConfig.retry().retryType()) {
            case RetryType.CONSTANT -> constantRetry();
            case RetryType.EXPONENTIAL -> exponentialRetry();
            case RetryType.LINEAR -> linearRetry();
        };
    }

    private Retry constantRetry() {
        return Retry.fixedDelay(
            applicationConfig.retry().attempts(),
            Duration.ofMillis(applicationConfig.retry().delay())
        ).filter(retryableErrorsFilter(applicationConfig.retry().statuses()));
    }

    private Retry exponentialRetry() {
        return Retry.backoff(
            applicationConfig.retry().attempts(),
            Duration.ofMillis(applicationConfig.retry().delay())
        ).filter(retryableErrorsFilter(applicationConfig.retry().statuses()));
    }

    private Retry linearRetry() {
        return Retry.from(retrySignalFlux -> Flux.deferContextual(
                contextView -> retrySignalFlux
                    .contextWrite(contextView)
                    .concatMap(retrySignal -> {
                        Retry.RetrySignal retrySignalCopy = retrySignal.copy();
                        Throwable currentFailure = retrySignalCopy.failure();
                        long iteration = retrySignalCopy.totalRetries();
                        if (currentFailure == null) {
                            return Mono.error(
                                new IllegalStateException("RetrySignal must not be null")
                            );
                        } else if (!retryableErrorsFilter(applicationConfig.retry().statuses()).test(currentFailure)) {
                            return Mono.error(currentFailure);
                        } else if (iteration >= applicationConfig.retry().attempts()) {
                            return Mono.error(new ExhaustedRetryException("retry failed on iteration " + iteration));
                        }
                        return Mono.delay(
                            Duration.ofMillis(applicationConfig.retry().delay()).multipliedBy(2 * iteration),
                            Schedulers.parallel()
                        );
                    })
            )
        );
    }

    private Predicate<Throwable> retryableErrorsFilter(List<Integer> codes) {
        return exception -> {
            if (exception instanceof HttpClientErrorException httpClientErrorException) {
                return codes.contains(httpClientErrorException.getStatusCode().value());
            } else {
                return false;
            }
        };
    }
}
