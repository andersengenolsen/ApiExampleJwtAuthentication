package anders.olsen.api.repository;

import anders.olsen.api.entity.Role;
import anders.olsen.api.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for accessing {@link anders.olsen.api.entity.Role} in database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);

}
