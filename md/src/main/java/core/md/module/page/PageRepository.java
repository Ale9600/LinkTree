package core.md.module.page;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface PageRepository extends CrudRepository<Page, Long> {
    Optional<Page> findByCode(String code);

    Optional<Page> findByDescriptionAndIdNotEquals(String pDescription, Long pId);

    Optional<Page> findByCodeAndIdNotEquals(String pCode, Long pId);

    @Query(value = """
                        SELECT  page.id,
                                page.code,
                                page.status,
                                page.description,
                              
                                page.insert_date as insertDate,
                                page.last_updated_date as lastUpdatedDate,
                                fn_get_status_description('ST_PAGE', page.status, :pLanguageCode) as statusDescription,
                                '' as message
                        FROM md_page page
                        WHERE page.id = :pId
                    """,
            nativeQuery = true)
    Optional<PageDto> findByIdWithStatusDescription(Long pId, String pLanguageCode);
}