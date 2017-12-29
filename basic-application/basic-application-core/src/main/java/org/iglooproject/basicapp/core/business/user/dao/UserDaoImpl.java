package org.iglooproject.basicapp.core.business.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import org.iglooproject.basicapp.core.business.user.model.QUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.business.person.dao.GenericUserDaoImpl;
import org.iglooproject.spring.util.StringUtils;

@Repository("personDao")
public class UserDaoImpl extends GenericUserDaoImpl<User> implements IUserDao {

	private final QUser qUser = QUser.user;

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
	public List<User> listByUserName(String userName) {
		return super.listByField(QUser.user, QUser.user.userName, userName, QUser.user.id.desc());
	}

}
