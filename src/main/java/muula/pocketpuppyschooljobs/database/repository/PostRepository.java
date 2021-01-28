package muula.pocketpuppyschooljobs.database.repository;

import java.util.List;
import muula.pocketpuppyschooljobs.database.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post "
        + "WHERE ID > :maxId "
        + "ORDER BY ID ASC "
        + "LIMIT 1 ;",
        nativeQuery = true)
    List<Post> findNextPost(@Param("maxId") Long maxId);

}