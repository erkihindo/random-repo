package muula.pocketpuppyschooljobs.database.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregationBodyDto {

    Map<AggregationType, Object> aggregations;

    public Number fetchNumberAggregation(AggregationType aggregationType) {
        return ((Number) fetchAggregation(aggregationType));
    }

    public Object fetchAggregation(AggregationType aggregationType) {
        return Optional.ofNullable(aggregations)
            .map(agg -> agg.getOrDefault(aggregationType, defaultValueForGivenType(aggregationType.getClassType())))
            .orElse(defaultValueForGivenType(aggregationType.getClassType()));
    }

    private Object defaultValueForGivenType(Class classType) {
        if (classType == Number.class) {
            return 0L;
        }

        if (classType == String.class) {
            return null;
        }

        return null;
    }

    public void updateNumberValue(AggregationType aggregationType, int moreOrLess) {
        if (aggregations == null) aggregations = new HashMap<>();

        if (aggregationType.getClassType() != Number.class) {
            return;
        }

        Number oldValue = (Number) aggregations.getOrDefault(aggregationType, 0L);
        Number newValue = oldValue.longValue() + moreOrLess;
        aggregations.put(aggregationType, newValue);
    }

    public void updateStringValue(AggregationType aggregationType, String newValue) {
        if (aggregations == null) aggregations = new HashMap<>();

        if (aggregationType.getClassType() != String.class) {
            return;
        }
        aggregations.put(aggregationType, newValue);
    }
}
