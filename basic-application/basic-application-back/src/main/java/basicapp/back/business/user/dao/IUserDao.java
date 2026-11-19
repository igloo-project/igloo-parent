package basicapp.back.business.user.dao;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import java.util.List;
import java.util.Optional;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface IUserDao extends IGenericEntityDao<Long, User> {

  Optional<User> getByUsernameCaseInsensitive(String username);

  Optional<User> getByEmailCaseInsensitive(EmailAddress emailAddress);

  List<User> listByUsername(String username);
}
