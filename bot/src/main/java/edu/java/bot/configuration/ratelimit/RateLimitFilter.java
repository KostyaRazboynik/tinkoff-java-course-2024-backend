package edu.java.bot.configuration.ratelimit;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final Bucket bucket;
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (ipBuckets
            .computeIfAbsent(request.getRemoteAddr(), k -> bucket)
            .tryConsumeAndReturnRemaining(1)
            .isConsumed()
        ) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().append(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        }
    }
}
