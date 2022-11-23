package base.foundation.module.coreentities.user;

import base.foundation.module.coreentities.company.Company;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String pName);
    Optional<User> findByEmail( String pEmail);
    Optional<User> findByCode( String pCode);
    Optional<User> findByCompany(Company company);

    @Query(value = """
                        SELECT  user.id id,
                                user.code code,
                                user.username username,
                                user.email email,
                                user.status status,
                                user.language_id languageId,
                                user.country_id countryId,
                                user.company_id companyId,
                                user.flag_enabled enabled,
                                user.version version,
                                user.insert_date insertDate,
                                user.last_updated_date lastUpdatedDate,
                                fn_get_status_description('ST_USER', user.status, :pLanguageCode) as statusDescription,
                                '' message,
                                '' password
                        FROM core_user user
                        WHERE user.id = :pId
                    """,
            nativeQuery = true)
    Optional<UserDto> findByIdWithStatusDescription(Long pId, String pLanguageCode);
}