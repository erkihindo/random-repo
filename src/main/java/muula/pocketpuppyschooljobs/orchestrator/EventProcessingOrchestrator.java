package muula.pocketpuppyschooljobs.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muula.pocketpuppyschooljobs.service.action.UserActionEventProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"!integration"})
public class EventProcessingOrchestrator {

    private final UserActionEventProcessor userActionEventProcessor;

    @Scheduled(initialDelay = 30000, fixedDelay = 30000)
    public void run() {
        log.info("Running event processing");
        userActionEventProcessor.processNextEvent();
    }
}
