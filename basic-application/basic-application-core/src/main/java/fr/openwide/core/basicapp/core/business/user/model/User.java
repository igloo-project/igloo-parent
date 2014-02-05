package fr.openwide.core.basicapp.core.business.user.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.jpa.security.business.person.model.GenericSimpleUser;
import fr.openwide.core.spring.util.StringUtils;

@Indexed
@Bindable
@Cacheable
@Entity(name = "user_")
public class User extends GenericSimpleUser<User, UserGroup> {
	
	private static final long serialVersionUID = 1508647513049577617L;
	
	public static final int MIN_PASSWORD_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 15;
	
	public User() {
		super();
	}

	@Transient
	@Override
	public String getFullName() {
		String fullName = super.getFullName();
		if (StringUtils.hasText(fullName)) {
			return fullName;
		} else {
			return getEmail();
		}
	}

}
