package muula.pocketpuppyschooljobs.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muula.pocketpuppyschooljobs.service.post.PostThumbnailService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"!integration"})
public class BlurCalculatorOrchestrator {

    private final PostThumbnailService postThumbnailService;

    @Scheduled(initialDelay = 31000, fixedDelay = 60000)
    public void run() {
        postThumbnailService.tryAndUpdateThumbnail();
    }
}
