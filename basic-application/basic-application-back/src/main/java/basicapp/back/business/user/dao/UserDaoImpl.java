package basicapp.back.business.user.dao;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.QUser;
import basicapp.back.business.user.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl extends GenericEntityDaoImpl<Long, User> implements IUserDao {

  private static final QUser qUser = QUser.user;

  @Override
  public User getByUsernameCaseInsensitive(String username) {
    return new JPAQuery<User>(getEntityManager())
        .from(qUser)
        .where(qUser.username.lower().eq(username.toLowerCase()))
        .fetchOne();
  }

  @Override
  public User getByEmailCaseInsensitive(EmailAddress emailAddress) {
    return new JPAQuery<User>(getEntityManager())
        .from(qUser)
        .where(qUser.emailAddress.eq(emailAddress))
        .fetchOne();
  }

  @Override
  public List<User> listByUsername(String username) {
    return super.listByField(QUser.user, QUser.user.username, username, QUser.user.id.desc());
  }
}
