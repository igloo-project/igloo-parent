package basicapp.back.business.user.repository;

import basicapp.back.business.user.model.User;
import java.util.Optional;

public interface CustomUserRepository {
  Optional<User> findByNaturalId(String username);
}
