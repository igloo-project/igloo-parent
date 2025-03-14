package basicapp.back.business.user.dao;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import java.util.List;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface IUserDao extends IGenericEntityDao<Long, User> {

  User getByUsernameCaseInsensitive(String username);

  User getByEmailCaseInsensitive(EmailAddress emailAddress);

  List<User> listByUsername(String username);
}
