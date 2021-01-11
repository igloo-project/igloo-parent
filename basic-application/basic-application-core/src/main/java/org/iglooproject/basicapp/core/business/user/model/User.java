package org.iglooproject.basicapp.core.business.user.model;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserAnnouncementInformation;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserPasswordInformation;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserPasswordRecoveryRequest;
import org.iglooproject.jpa.security.business.person.model.GenericSimpleUser;
import org.iglooproject.spring.util.StringUtils;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;

@Indexed
@Bindable
@Cacheable
@Inheritance(strategy = InheritanceType.JOINED)
@Entity(name = "user_")
public class User extends GenericSimpleUser<User, UserGroup> {

	private static final long serialVersionUID = 1508647513049577617L;

	public static final int MIN_PASSWORD_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 15;

	@Embedded
	private UserPasswordInformation passwordInformation;

	@Embedded
	private UserPasswordRecoveryRequest passwordRecoveryRequest;

	@Embedded
	private UserAnnouncementInformation announcementInformation = new UserAnnouncementInformation();

	public User() {
		super();
	}

	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public UserPasswordInformation getPasswordInformation() {
		if (passwordInformation == null) {
			passwordInformation = new UserPasswordInformation();
		}
		return passwordInformation;
	}

	public UserPasswordRecoveryRequest getPasswordRecoveryRequest() {
		if (passwordRecoveryRequest == null) {
			passwordRecoveryRequest = new UserPasswordRecoveryRequest();
		}
		return passwordRecoveryRequest;
	}

	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public UserAnnouncementInformation getAnnouncementInformation() {
		if (announcementInformation == null) {
			announcementInformation = new UserAnnouncementInformation();
		}
		return announcementInformation;
	}

	public void setAnnouncementInformation(UserAnnouncementInformation announcementInformation) {
		this.announcementInformation = announcementInformation;
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
