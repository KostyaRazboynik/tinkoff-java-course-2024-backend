package edu.java.service.scheduler;

import edu.java.domain.repository.LinkRepository;
import edu.java.service.scheduler.source.GitHubLinkUpdater;
import edu.java.service.scheduler.source.StackOverflowLinkUpdater;
import edu.java.utils.LinkValidator;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final LinkRepository linkRepository;
    private final GitHubLinkUpdater gitHubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;

    @Override
    public int update() {
        AtomicInteger countUpdatedLinks = new AtomicInteger();

        linkRepository.getLinksToUpdate().forEach(link -> {
            if (LinkValidator.isGitHubLink(link.link)) {
                countUpdatedLinks.addAndGet(gitHubLinkUpdater.update(link));
            } else if (LinkValidator.isStackOverflowLink(link.link)) {
                countUpdatedLinks.addAndGet(stackOverflowLinkUpdater.update(link));
            }
        });

        return countUpdatedLinks.get();
    }
}
