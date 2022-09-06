package muula.pocketpuppyschooljobs.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"!integration"})
public class RandomOrchestrator {


    @Scheduled(fixedDelay = 1000)
    public void run() {
        log.info("I am running");
    }
}
