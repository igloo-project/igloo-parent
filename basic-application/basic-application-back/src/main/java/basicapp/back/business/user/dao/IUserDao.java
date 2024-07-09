package basicapp.back.business.user.dao;

import java.util.List;

import org.iglooproject.jpa.security.business.user.dao.IGenericUserDao;

import basicapp.back.business.user.model.User;

public interface IUserDao extends IGenericUserDao<User> {

	User getByEmailCaseInsensitive(String email);

	List<User> listByUsername(String username);

}
