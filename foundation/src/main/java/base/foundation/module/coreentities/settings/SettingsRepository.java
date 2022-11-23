package base.foundation.module.coreentities.settings;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends CrudRepository<Settings, Long> {
    Optional<Settings> findByCode(String code);

    @Query(value = """
                        SELECT  settings.id,
                                settings.code,
                                settings.status,
                                settings.description,
                                settings.flag_enabled as enabled,
                                settings.insert_date as insertDate,
                                settings.last_updated_date as lastUpdatedDate,
                                fn_get_status_description('ST_SETTINGS', settings.status, :pLanguageCode) as statusDescription,
                                '' as message
                        FROM core_settings settings
                        WHERE settings.id = :pId
                    """,
            nativeQuery = true)
    Optional<SettingsDto> findByIdWithStatusDescription(Long pId, String pLanguageCode);

    Optional<Settings> findByKey(String key);
}