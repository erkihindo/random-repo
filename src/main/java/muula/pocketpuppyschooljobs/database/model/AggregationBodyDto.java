package muula.pocketpuppyschooljobs.database.model;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregationBodyDto {

    Map<AggregationType, Long> aggregations;

    public void updateValue(AggregationType aggregationType, int moreOrLess) {
        if (aggregations == null) aggregations = new HashMap<>();

        Long oldValue = aggregations.getOrDefault(aggregationType, 0L);
        Long newValue = oldValue + moreOrLess;
        aggregations.put(aggregationType, newValue);
    }
}
