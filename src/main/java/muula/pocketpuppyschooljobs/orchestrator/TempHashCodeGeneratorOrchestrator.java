package muula.pocketpuppyschooljobs.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muula.pocketpuppyschooljobs.service.post.HashCodeGeneratorService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"!integration"})
public class TempHashCodeGeneratorOrchestrator {

    private final HashCodeGeneratorService hashCodeGeneratorService;

    @Scheduled(initialDelay = 10000, fixedDelay = 1000)
    public void run() {
        hashCodeGeneratorService.generateHash();
    }
}
