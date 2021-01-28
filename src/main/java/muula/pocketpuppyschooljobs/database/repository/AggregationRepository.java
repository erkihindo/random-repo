package muula.pocketpuppyschooljobs.database.repository;

import java.util.Optional;
import muula.pocketpuppyschooljobs.database.entity.Aggregation;
import muula.pocketpuppyschooljobs.database.entity.enumerated.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AggregationRepository extends JpaRepository<Aggregation, Long> {

    Optional<Aggregation> findByTargetIdAndTargetType(String id, EntityType type);
}