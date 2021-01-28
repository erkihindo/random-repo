package muula.pocketpuppyschooljobs.database.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
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
import muula.pocketpuppyschooljobs.database.entity.enumerated.UserActionType;
import org.springframework.data.annotation.PersistenceConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table(name = "user_action")
public class UserAction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    UserActionType type;

    @Column(name = "source_id")
    String sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    EntityType sourceType;

    @Column(name = "target_id")
    String targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    EntityType targetType;

    @Column(name = "body")
    String body;

    @Column(name = "created_at")
    Date createdAt;

}
