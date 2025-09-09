package basicapp.back.business.role.repository;

import basicapp.back.business.role.model.Role;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IRoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

  List<Role> findAllByIdInOrderById(Collection<Long> ids);

  Optional<Role> findByTitle(String title);
}
