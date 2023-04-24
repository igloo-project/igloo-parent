package org.iglooproject.basicapp.core.business.user.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserAnnouncementInformation;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserPasswordInformation;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserPasswordRecoveryRequest;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;
import org.iglooproject.spring.util.StringUtils;

@Indexed
@Bindable
@Cacheable
@Inheritance(strategy = InheritanceType.JOINED)
@Entity(name = "user_")
public class User extends GenericSimpleUser<User, UserGroup> {

	private static final long serialVersionUID = 1508647513049577617L;

	@Embedded
	private UserPasswordInformation passwordInformation;

	@Embedded
	private UserPasswordRecoveryRequest passwordRecoveryRequest;

	@Embedded
	private UserAnnouncementInformation announcementInformation = new UserAnnouncementInformation();

	public User() {
		super();
	}

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
