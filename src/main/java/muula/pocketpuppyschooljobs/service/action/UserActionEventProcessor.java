package muula.pocketpuppyschooljobs.service.action;


import static muula.pocketpuppyschooljobs.database.entity.enumerated.SystemPropertyKey.USER_ACTION_MAX_ID;
import static muula.pocketpuppyschooljobs.database.model.AggregationType.*;
import static muula.pocketpuppyschooljobs.utils.JsonSerializer.JSON_SERIALIZER;

import java.time.ZonedDateTime;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionEventProcessor {

    private final SystemPropertyRepository systemPropertyRepository;
    private final UserActionRepository userActionRepository;
    private final PostRepository postRepository;
    private final AggregationRepository aggregationRepository;

    @Value("${service.reportThreshold}")
    private Long REPORT_THRESHOLD;

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
            case USER_PICTURE_EDIT:
            case POST_EDIT:
            case COMMENT_EDIT:
                break; // Useless events
            case LIKE:
                updateNumberAggregations(a.getTargetType(), a.getTargetId(), LIKE_COUNT_V1, 1);
                updateAggregation(a.getTargetId(), EntityType.POST, LATEST_LIKE_DATETIME, ZonedDateTime.now().toString());
                break;
            case COMMENT:
                updateNumberAggregations(a.getTargetType(), a.getTargetId(), COMMENT_COUNT_V1, 1);
                updateAggregation(a.getTargetId(), EntityType.POST, LATEST_COMMENT_DATETIME, ZonedDateTime.now().toString());
                break;
            case COMMENT_DELETE:
                updateNumberAggregations(a.getTargetType(), a.getTargetId(), COMMENT_COUNT_V1, -1);
                break;
            case POST:
                updateNumberAggregations(a.getSourceType(), a.getSourceId(), POST_COUNT_V1, 1);
                break;
            case POST_DELETE:
                updateNumberAggregations(a.getSourceType(), a.getSourceId(), POST_COUNT_V1, -1);
                break;
            case REPORT:
                updateNumberAggregations(a.getTargetType(), a.getTargetId(), REPORT_COUNT_V1, 1);
                checkIfTargetNeedsToBeDeleted(a);
                break;
            default:
                throw new RuntimeException("Unknown action was done!!!!");
        }
    }

    private void updateNumberAggregations(EntityType entityType, String entityId, AggregationType type, int change) {
        switch (entityType) {
            case USER:
                updateAggregation(entityId, EntityType.USER, type, change);
                break;
            case POST:
                updateAggregation(entityId, EntityType.POST, type, change);
                Post post = postRepository.findById(Long.valueOf(entityId)).orElseThrow(() -> new RuntimeException("Action was present but entry is missing in db: " + entityId));
                updateAggregation(String.valueOf(post.getUserId()), EntityType.USER, type, change);
                break;
            default:
                throw new RuntimeException("Unknown entity type");
        }
    }

    private void checkIfTargetNeedsToBeDeleted(UserAction userAction) {
        switch (userAction.getTargetType()) {
            case POST:
                if (areThereMoreReportsThanLikes(userAction.getTargetId(), userAction.getTargetType())) {
                    Post post = postRepository.findById(Long.valueOf(userAction.getTargetId())).orElseThrow(() -> new RuntimeException("Action was present but entry is missing in db: " + userAction.getId()));
                    log.info("Hiding post because of reports, id: {}", post.getId());
                    post.setIsHidden(Boolean.TRUE);
                    postRepository.save(post);
                }
                break;
            default:
                throw new RuntimeException("Unmapped entity type for checking deletion: " + userAction.getTargetType());
        }
    }

    private boolean areThereMoreReportsThanLikes(String id, EntityType entityType) {
        Aggregation aggregation = aggregationRepository.findByTargetIdAndTargetType(id, entityType).orElseThrow(() -> new RuntimeException("Entity was reported but no aggregation row exists: " + id + "; " + entityType));

        AggregationBodyDto bodyDto = aggregation.convertBodyToDto();
        if (bodyDto.fetchNumberAggregation(REPORT_COUNT_V1).longValue() > REPORT_THRESHOLD) {
            if (bodyDto.fetchNumberAggregation(REPORT_COUNT_V1).longValue() > bodyDto.fetchNumberAggregation(LIKE_COUNT_V1).longValue()) {
                return true;
            }
        }

        return false;
    }

    private void updateAggregation(String id, EntityType entityType, AggregationType aggregationType, Object change) {
        Aggregation aggregation = aggregationRepository.findByTargetIdAndTargetType(id, entityType).orElse(null);

        if (aggregation == null) {
            aggregation = new Aggregation(null, id, entityType, JSON_SERIALIZER.writeAsJson(new AggregationBodyDto(new HashMap<>())), new Date(), new Date());
        }

        AggregationBodyDto bodyDto = aggregation.convertBodyToDto();
        if (change instanceof Number) {
            bodyDto.updateNumberValue(aggregationType, ((Number) change).intValue());
        }
        if (change instanceof String) {
            bodyDto.updateStringValue(aggregationType, (String) change);
        }

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
