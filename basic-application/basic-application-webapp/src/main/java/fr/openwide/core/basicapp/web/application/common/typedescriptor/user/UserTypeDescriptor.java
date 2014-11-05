package fr.openwide.core.basicapp.web.application.common.typedescriptor.user;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.AbstractGenericEntityTypeDescriptor;

public abstract class UserTypeDescriptor<U extends User> extends AbstractGenericEntityTypeDescriptor<UserTypeDescriptor<U>, U> {

	private static final long serialVersionUID = -349656773642244352L;

	public static <U extends User> UserTypeDescriptor<U> get(U entity) {
		return (UserTypeDescriptor<U>) AbstractGenericEntityTypeDescriptor.<UserTypeDescriptor<U>, U>get(UserTypeDescriptor.class, entity);
	}

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
	};

	protected UserTypeDescriptor(Class<U> clazz, String name) {
		super(UserTypeDescriptor.class, clazz, name);
	}

	public abstract AdministrationUserTypeDescriptor<U> administrationTypeDescriptor();

	public abstract SecurityUserTypeDescriptor<U> securityTypeDescriptor();

	public abstract NotificationUserTypeDescriptor<U> notificationTypeDescriptor();

}
