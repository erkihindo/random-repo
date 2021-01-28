package muula.pocketpuppyschooljobs.database.repository;

import java.util.List;
import muula.pocketpuppyschooljobs.database.entity.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    @Query(value = "SELECT * FROM user_action "
        + "WHERE ID > :maxId "
        + "ORDER BY ID ASC "
        + "LIMIT 100 ;",
        nativeQuery = true)
    List<UserAction> findNextActions(@Param("maxId") Long maxId);
}