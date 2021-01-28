package muula.pocketpuppyschooljobs.database.repository;

import java.util.Optional;
import muula.pocketpuppyschooljobs.database.entity.SystemProperty;
import muula.pocketpuppyschooljobs.database.entity.enumerated.SystemPropertyKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SystemPropertyRepository extends CrudRepository<SystemProperty, Long> {
    Optional<SystemProperty> findFirstByKeyString(SystemPropertyKey key);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO system_property (key_string, value) " +
                   "VALUES (:key_string, :value_string) " +
                   "ON DUPLICATE KEY UPDATE value = :value_string",
            nativeQuery = true)
    void setValueByKey(@Param("key_string") String key, @Param("value_string") String valueString);
}
