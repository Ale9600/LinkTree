package base.foundation.module.coreentities.company;

import base.foundation.module.coreentities.role.RoleDto;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findByCode(String code);

    Optional<Company> findByDescriptionAndIdNotEquals(String pDescription, Long pId);

    Optional<Company> findByCodeAndIdNotEquals(String pCode, Long pId);

    @Query(value = """
                        SELECT  company.id,
                                company.code,
                                company.status,
                                company.description,
                                company.vat_number vatNumber,
                                company.address,
                                company.email,
                                company.business_name businessName,
                                company.country_id countryId,
                                company.phone_number phoneNumber,
                                company.flag_enabled as enabled,
                                company.insert_date as insertDate,
                                company.last_updated_date as lastUpdatedDate,
                                fn_get_status_description('ST_COMPANY', company.status, :pLanguageCode) as statusDescription,
                                '' as message
                        FROM core_company company
                        WHERE company.id = :pId
                    """,
            nativeQuery = true)
    Optional<CompanyDto> findByIdWithStatusDescription(Long pId, String pLanguageCode);
}