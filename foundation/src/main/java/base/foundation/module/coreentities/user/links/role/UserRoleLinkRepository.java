package base.foundation.module.coreentities.user.links.role;

import base.foundation.module.coreentities.role.Role;
import base.foundation.module.coreentities.user.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UserRoleLinkRepository extends CrudRepository<UserRoleLink, Long> {
    List<UserRoleLink> findByUser(User user);
    List<UserRoleLink> findByRole(Role role);
}