package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

import fr.openwide.core.basicapp.core.business.user.model.QUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.dao.GenericUserDaoImpl;
import fr.openwide.core.spring.util.StringUtils;

@Repository("personDao")
public class UserDaoImpl extends GenericUserDaoImpl<User> implements IUserDao {

	private final QUser qUser = QUser.user;

	public UserDaoImpl() {
		super();
	}

	@Override
	public User getByEmailCaseInsensitive(String email) {
		return new JPAQuery(getEntityManager())
				.from(qUser)
				.where(qUser.email.lower().eq(StringUtils.lowerCase(email)))
				.singleResult(qUser);
	}

	@Override
	public List<User> listByUserName(String userName) {
		return super.listByField(QUser.user, QUser.user.userName, userName, QUser.user.id.desc());
	}

}
