package muula.pocketpuppyschooljobs.service.action;


import static muula.pocketpuppyschooljobs.database.entity.enumerated.SystemPropertyKey.USER_ACTION_MAX_ID;
import static muula.pocketpuppyschooljobs.database.model.AggregationType.*;
import static muula.pocketpuppyschooljobs.utils.JsonSerializer.JSON_SERIALIZER;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import muula.pocketpuppyschooljobs.database.entity.Aggregation;
import muula.pocketpuppyschooljobs.database.entity.Post;
import muula.pocketpuppyschooljobs.database.entity.UserAction;
import muula.pocketpuppyschooljobs.database.entity.enumerated.EntityType;
import muula.pocketpuppyschooljobs.database.model.AggregationBodyDto;
import muula.pocketpuppyschooljobs.database.model.AggregationType;
import muula.pocketpuppyschooljobs.database.repository.AggregationRepository;
import muula.pocketpuppyschooljobs.database.repository.PostRepository;
import muula.pocketpuppyschooljobs.database.repository.SystemPropertyRepository;
import muula.pocketpuppyschooljobs.database.repository.UserActionRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionEventProcessor {

    private final SystemPropertyRepository systemPropertyRepository;
    private final UserActionRepository userActionRepository;
    private final PostRepository postRepository;
    private final AggregationRepository aggregationRepository;

    public void processNextEvent() {
        Long maxProcessedId = getMaxId();

        List<UserAction> actionsToProcess = getNextRowsToProcess(maxProcessedId);
        if (actionsToProcess.isEmpty()) return;

        process(actionsToProcess);

        updatePointer(actionsToProcess);

    }

    private void process(List<UserAction> actionsToProcess) {
        actionsToProcess.forEach(
            this::process
        );
    }

    private void process(UserAction a) {
        switch (a.getType()) {
            case SIGN_UP:
            case POST_EDIT:
            case COMMENT_EDIT:
                break; // Useless events
            case LIKE:
                updateTargetAggregations(a, LIKE_COUNT_V1, 1);
                break;
            case COMMENT:
                updateTargetAggregations(a, COMMENT_COUNT_V1, 1);
                break;
            case POST:
                updateSourceAggregations(a, POST_COUNT_V1, 1);
                break;
            case POST_DELETE:
                updateSourceAggregations(a, POST_COUNT_V1, -1);
                break;
            case COMMENT_DELETE:
                updateTargetAggregations(a, COMMENT_COUNT_V1, -1);
                break;
            case REPORT:
                updateTargetAggregations(a, REPORT_COUNT_V1, 1);
                break;
            default:
                throw new RuntimeException("Unknown action was done!!!!");
        }
    }

    private void updateSourceAggregations(UserAction userAction, AggregationType type, int moreOrLess) {
        switch (userAction.getSourceType()) {
            case USER:
                updateAggregation(userAction.getSourceId(), EntityType.USER, type, moreOrLess);
                break;
            case POST:
                updateAggregation(userAction.getSourceId(), EntityType.POST, type, moreOrLess);
                Post post = postRepository.findById(Long.valueOf(userAction.getSourceId())).orElseThrow(() -> new RuntimeException("Action was present but entry is missing in db: " + userAction.getId()));
                updateAggregation(String.valueOf(post.getUserId()), EntityType.USER, type, moreOrLess);
                break;
            default:
                throw new RuntimeException("Unknown entity type");
        }
    }

    private void updateTargetAggregations(UserAction userAction, AggregationType type, int moreOrLess) {
        switch (userAction.getTargetType()) {
            case USER:
                updateAggregation(userAction.getTargetId(), EntityType.USER, type, moreOrLess);
                break;
            case POST:
                updateAggregation(userAction.getTargetId(), EntityType.POST, type, moreOrLess);
                Post post = postRepository.findById(Long.valueOf(userAction.getTargetId())).orElseThrow(() -> new RuntimeException("Action was present but entry is missing in db: " + userAction.getId()));
                updateAggregation(String.valueOf(post.getUserId()), EntityType.USER, type, moreOrLess);
                break;
            default:
                throw new RuntimeException("Unknown entity type");
        }

    }

    private void updateAggregation(String id, EntityType entityType, AggregationType aggregationType, int moreOrLess) {
        Aggregation aggregation = aggregationRepository.findByTargetIdAndTargetType(id, entityType).orElse(null);

        if (aggregation == null) {
            aggregation = new Aggregation(null, id, entityType, JSON_SERIALIZER.writeAsJson(new AggregationBodyDto(new HashMap<>())), new Date(), new Date());
        }

        AggregationBodyDto bodyDto = aggregation.convertBodyToDto();
        bodyDto.updateValue(aggregationType, moreOrLess);
        aggregation.setBody(JSON_SERIALIZER.writeAsJson(bodyDto));
        aggregationRepository.save(aggregation);
    }

    private Long getMaxId() {
        val optionalRow = systemPropertyRepository.findFirstByKeyString(USER_ACTION_MAX_ID);

        if (!optionalRow.isPresent()) {
            return 0L;
        } else { return Long.valueOf(optionalRow.get().getValue()); }
    }

    private List<UserAction> getNextRowsToProcess(Long maxProcessedId) {
        return userActionRepository.findNextActions(maxProcessedId);
    }

    private void updatePointer(List<UserAction> userActions) {
        Long maxId = userActions.stream()
            .map(UserAction::getId)
            .max(Long::compare)
            .get();
        log.info("Updated actions until: {}", maxId);

        systemPropertyRepository.setValueByKey(USER_ACTION_MAX_ID.name(), maxId.toString());
    }
}
