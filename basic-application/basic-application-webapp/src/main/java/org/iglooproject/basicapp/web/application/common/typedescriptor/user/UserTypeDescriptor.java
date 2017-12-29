package org.iglooproject.basicapp.web.application.common.typedescriptor.user;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.search.IBasicUserSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.IGenericUserSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.ITechnicalUserSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.IUserSearchQuery;
import org.iglooproject.basicapp.web.application.common.typedescriptor.AbstractGenericEntityTypeDescriptor;
import org.iglooproject.wicket.more.application.CoreWicketApplication;

public abstract class UserTypeDescriptor<U extends User> extends AbstractGenericEntityTypeDescriptor<UserTypeDescriptor<U>, U> {

	private static final long serialVersionUID = -349656773642244352L;

	public static final UserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new UserTypeDescriptor<TechnicalUser>(TechnicalUser.class, "technicalUser") {
		private static final long serialVersionUID = 1L;

		@Override
		protected Object readResolve() {
			return TECHNICAL_USER;
		}

		@Override
		public AdministrationUserTypeDescriptor<TechnicalUser> administrationTypeDescriptor() {
			return AdministrationUserTypeDescriptor.TECHNICAL_USER;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SecurityUserTypeDescriptor<TechnicalUser> securityTypeDescriptor() {
			return (SecurityUserTypeDescriptor<TechnicalUser>) SecurityUserTypeDescriptor.USER;
		}

		@SuppressWarnings("unchecked")
		@Override
		public NotificationUserTypeDescriptor<TechnicalUser> notificationTypeDescriptor() {
			return (NotificationUserTypeDescriptor<TechnicalUser>) NotificationUserTypeDescriptor.USER;
		}
		
		@Override
		public IGenericUserSearchQuery<TechnicalUser> newSearchQuery() {
			return CoreWicketApplication.get().getApplicationContext().getBean(ITechnicalUserSearchQuery.class);
		}
	};

	public static final UserTypeDescriptor<BasicUser> BASIC_USER = new UserTypeDescriptor<BasicUser>(BasicUser.class, "basicUser") {
		private static final long serialVersionUID = 1L;

		@Override
		protected Object readResolve() {
			return BASIC_USER;
		}

		@Override
		public AdministrationUserTypeDescriptor<BasicUser> administrationTypeDescriptor() {
			return AdministrationUserTypeDescriptor.BASIC_USER;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SecurityUserTypeDescriptor<BasicUser> securityTypeDescriptor() {
			return (SecurityUserTypeDescriptor<BasicUser>) SecurityUserTypeDescriptor.USER;
		}

		@SuppressWarnings("unchecked")
		@Override
		public NotificationUserTypeDescriptor<BasicUser> notificationTypeDescriptor() {
			return (NotificationUserTypeDescriptor<BasicUser>) NotificationUserTypeDescriptor.USER;
		}
		
		@Override
		public IGenericUserSearchQuery<BasicUser> newSearchQuery() {
			return CoreWicketApplication.get().getApplicationContext().getBean(IBasicUserSearchQuery.class);
		}
	};

	public static final UserTypeDescriptor<User> USER = new UserTypeDescriptor<User>(User.class, "user") {
		private static final long serialVersionUID = 1L;

		@Override
		protected Object readResolve() {
			return USER;
		}

		@Override
		public AdministrationUserTypeDescriptor<User> administrationTypeDescriptor() {
			throw new IllegalStateException("child type descriptor not available for type descriptor " + this);
		}

		@SuppressWarnings("unchecked")
		@Override
		public SecurityUserTypeDescriptor<User> securityTypeDescriptor() {
			return (SecurityUserTypeDescriptor<User>) SecurityUserTypeDescriptor.USER;
		}

		@SuppressWarnings("unchecked")
		@Override
		public NotificationUserTypeDescriptor<User> notificationTypeDescriptor() {
			return (NotificationUserTypeDescriptor<User>) NotificationUserTypeDescriptor.USER;
		}

		@Override
		public IGenericUserSearchQuery<User> newSearchQuery() {
			return CoreWicketApplication.get().getApplicationContext().getBean(IUserSearchQuery.class);
		}
	};

	protected UserTypeDescriptor(Class<U> clazz, String name) {
		super(UserTypeDescriptor.class, clazz, name);
	}

	public static <U extends User> UserTypeDescriptor<U> get(U entity) {
		return (UserTypeDescriptor<U>) AbstractGenericEntityTypeDescriptor.<UserTypeDescriptor<U>, U>get(UserTypeDescriptor.class, entity);
	}

	public abstract AdministrationUserTypeDescriptor<U> administrationTypeDescriptor();

	public abstract SecurityUserTypeDescriptor<U> securityTypeDescriptor();

	public abstract NotificationUserTypeDescriptor<U> notificationTypeDescriptor();
	
	public abstract IGenericUserSearchQuery<U> newSearchQuery();

}
