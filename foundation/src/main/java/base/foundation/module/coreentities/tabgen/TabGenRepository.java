package base.foundation.module.coreentities.tabgen;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface TabGenRepository extends CrudRepository<TabGen, Long> {
    Optional<TabGen> findByCode(String code);
}