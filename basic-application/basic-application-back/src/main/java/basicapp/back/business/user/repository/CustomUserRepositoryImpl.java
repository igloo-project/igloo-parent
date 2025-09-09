package basicapp.back.business.user.repository;

import basicapp.back.business.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.hibernate.Session;

public class CustomUserRepositoryImpl implements CustomUserRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Optional<User> findByNaturalId(String username) {
    return entityManager.unwrap(Session.class).bySimpleNaturalId(User.class).loadOptional(username);
  }
}
