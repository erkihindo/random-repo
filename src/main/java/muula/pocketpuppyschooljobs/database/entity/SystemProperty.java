package muula.pocketpuppyschooljobs.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import muula.pocketpuppyschooljobs.database.entity.enumerated.SystemPropertyKey;
import org.springframework.data.annotation.PersistenceConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table(name = "system_property")
public class SystemProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "key_string")
    SystemPropertyKey keyString;

    @Column(name = "value")
    String value;
}
