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
import muula.pocketpuppyschooljobs.database.entity.enumerated.PostType;
import org.springframework.data.annotation.PersistenceConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "link")
    String link;

    @Column(name = "hash_id")
    String hashId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    PostType type;

    @Column(name = "blur_hash")
    String blurHash;

    @Column(name = "description")
    String description;

    @Column(name = "extension")
    String extension;

    @Column(name = "height")
    Long height;

    @Column(name = "width")
    Long width;

    @Column(name = "is_hidden")
    Boolean isHidden;

    @Column(name = "created_at")
    Date createdAt;
}
