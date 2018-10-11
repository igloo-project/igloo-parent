package org.iglooproject.basicapp.core.business.user.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserPasswordInformation;
import org.iglooproject.basicapp.core.business.user.model.embeddable.UserPasswordRecoveryRequest;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.security.business.person.model.GenericSimpleUser;
import org.iglooproject.spring.util.StringUtils;

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

	@Basic(optional = false)
	private boolean openGeneralMessage = true;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastGeneralMessageActionDate;

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

	public boolean isOpenGeneralMessage() {
		return openGeneralMessage;
	}

	public void setOpenGeneralMessage(boolean openGeneralMessage) {
		this.openGeneralMessage = openGeneralMessage;
	}

	public Date getLastGeneralMessageActionDate() {
		return CloneUtils.clone(lastGeneralMessageActionDate);
	}

	public void setLastGeneralMessageActionDate(Date lastGeneralMessageActionDate) {
		this.lastGeneralMessageActionDate = CloneUtils.clone(lastGeneralMessageActionDate);
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
