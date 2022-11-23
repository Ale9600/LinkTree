package base.foundation.module.coreentities.translation;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface TranslationRepository extends CrudRepository<Translation, Long> {
    Optional<Translation> findByCode(String code);
    Optional<Translation> findByKey(String pKey);

    @Query(value = """
                       SELECT  translation.id id,
                                translation.description description,
                                (SELECT tr.description
                                 FROM core_translation tr
                                     LEFT JOIN core_language lang
                                        ON tr.language_id = lang.id
                                 WHERE tr.key = :key and lang.code = :defaultLanguageCode) defaultDescription,
                                translation.status status,
                                '' message,
                                '' statusDescription,
                                translation.type type,
                                translation.code code,
                                translation.flag_enabled enabled,
                                translation.insert_date as insertDate,
                                translation.last_updated_date as lastUpdatedDate
                        FROM core_translation translation
                        LEFT JOIN core_language lang
                                        ON translation.language_id = lang.id
                        WHERE translation.key = :key AND lang.code = :languageCode
                    """, nativeQuery = true)
    Optional<TranslationDto> findByKeyAndLanguageOrDefaultLanguage(String key, String languageCode, String defaultLanguageCode);
}