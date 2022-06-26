package muula.pocketpuppyschooljobs.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muula.pocketpuppyschooljobs.service.post.SpamPostFindingService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"!integration"})
public class SpamPostFindingOrchestrator {

    private final SpamPostFindingService spamPostFindingService;

    @Scheduled(initialDelay = 30000, fixedDelay = 600000)
    public void run() {
        spamPostFindingService.hidePossibleSpamPost();
    }
}
