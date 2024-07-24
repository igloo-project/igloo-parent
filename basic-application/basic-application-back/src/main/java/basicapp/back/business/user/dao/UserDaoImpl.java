package basicapp.back.business.user.dao;

import basicapp.back.business.user.model.QUser;
import basicapp.back.business.user.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.iglooproject.jpa.security.business.user.dao.GenericUserDaoImpl;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl extends GenericUserDaoImpl<User> implements IUserDao {

  private static final QUser qUser = QUser.user;

  public UserDaoImpl() {
    super();
  }

  @Override
  public User getByEmailCaseInsensitive(String email) {
    return new JPAQuery<User>(getEntityManager())
        .from(qUser)
        .where(qUser.email.lower().eq(StringUtils.lowerCase(email)))
        .fetchOne();
  }

  @Override
  public List<User> listByUsername(String username) {
    return super.listByField(QUser.user, QUser.user.username, username, QUser.user.id.desc());
  }
}
