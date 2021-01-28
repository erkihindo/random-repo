package muula.pocketpuppyschooljobs.database.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import muula.pocketpuppyschooljobs.database.entity.enumerated.EntityType;
import muula.pocketpuppyschooljobs.database.model.AggregationBodyDto;
import muula.pocketpuppyschooljobs.utils.JsonSerializer;
import org.springframework.data.annotation.PersistenceConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table(name = "aggregation")
public class Aggregation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(name = "target_id")
    String targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    EntityType targetType;

    @Column(name = "body")
    String body;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updated_at;

    public AggregationBodyDto convertBodyToDto() {
        if (body == null) return new AggregationBodyDto(new HashMap<>());
        return JsonSerializer.JSON_SERIALIZER.readValue(body, AggregationBodyDto.class);
    }
}
