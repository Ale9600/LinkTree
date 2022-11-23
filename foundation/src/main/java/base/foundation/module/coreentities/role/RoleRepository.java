package base.foundation.module.coreentities.role;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByCode(String code);

    @Query(value = """
                        SELECT  role.id,
                                role.code,
                                role.status,
                                role.description,
                                role.flag_enabled as enabled,
                                role.insert_date as insertDate,
                                role.last_updated_date as lastUpdatedDate,
                                fn_get_status_description('ST_ROLE', role.status, :pLanguageCode) as statusDescription,
                                '' as message
                        FROM core_role role
                        WHERE role.id = :pId
                    """,
            nativeQuery = true)
    Optional<RoleDto> findByIdWithStatusDescription(Long pId, String pLanguageCode);

    Optional<Role> findByCodeAndIdNotEquals(String pCode, Long pId);
}