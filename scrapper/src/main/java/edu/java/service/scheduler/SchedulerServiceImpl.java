package edu.java.service.scheduler;

import edu.java.service.LinkUpdaterService;
import edu.java.service.scheduler.source.GitHubLinkUpdater;
import edu.java.service.scheduler.source.StackOverflowLinkUpdater;
import edu.java.utils.LinkValidator;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final LinkUpdaterService linkUpdaterService;
    private final GitHubLinkUpdater gitHubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;

    @Override
    public int update() {
        AtomicInteger countUpdatedLinks = new AtomicInteger();

        linkUpdaterService.getLinksToUpdate().forEach(link -> {
            if (LinkValidator.isGitHubLink(link.link)) {
                countUpdatedLinks.addAndGet(gitHubLinkUpdater.update(link));
            } else if (LinkValidator.isStackOverflowLink(link.link)) {
                countUpdatedLinks.addAndGet(stackOverflowLinkUpdater.update(link));
            }
            linkUpdaterService.updateCheckDate(link.link);
        });

        return countUpdatedLinks.get();
    }
}
