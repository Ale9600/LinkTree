package base.foundation.module.coreentities.language;

import base.foundation.module.coreentities.role.RoleDto;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
    Optional<Language> findByCode(String pCode);
    Optional<Language> findByCodeAndIdNotEquals(String pCode, Long pId);
    Optional<Language> findByDescription(String pDescription);
    Optional<Language> findByDescriptionAndIdNotEquals(String pDescription, Long pId);

    @Query(value = """
                        SELECT  language.id,
                                language.code,
                                language.status,
                                language.description,
                                language.flag_enabled as enabled,
                                language.insert_date as insertDate,
                                language.last_updated_date as lastUpdatedDate,
                                fn_get_status_description('ST_LANGUAGE', language.status, :pLanguageCode) as statusDescription,
                                '' as message
                        FROM core_language language
                        WHERE language.id = :pId
                    """,
            nativeQuery = true)
    Optional<LanguageDto> findByIdWithStatusDescription(Long pId, String pLanguageCode);
}