package basicapp.back.business.user.repository;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository
    extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, CustomUserRepository {

  Optional<User> findByUsernameIgnoreCase(String username);

  @Query(
      """
      SELECT u fROM user_ u
      WHERE LOWER(u.emailAddress) = LOWER(:#{#email.value})
      """)
  Optional<User> findByEmailAddressIgnoreCase(EmailAddress email);
}
