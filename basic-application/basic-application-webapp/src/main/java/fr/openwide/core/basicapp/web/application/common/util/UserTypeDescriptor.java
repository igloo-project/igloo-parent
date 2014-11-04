package fr.openwide.core.basicapp.web.application.common.util;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;

public abstract class UserTypeDescriptor<U extends User> extends AbstractGenericEntityTypeDescriptor<UserTypeDescriptor<U>, U> {

	private static final long serialVersionUID = -349656773642244352L;

	public static final UserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new UserTypeDescriptor<TechnicalUser>(TechnicalUser.class, "technicalUser") {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return TECHNICAL_USER;
		}
	};
	
	public static final UserTypeDescriptor<BasicUser> BASIC_USER = new UserTypeDescriptor<BasicUser>(BasicUser.class, "basicUser") {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return BASIC_USER;
		}
	};

	protected UserTypeDescriptor(Class<U> clazz, String name) {
		super(clazz, name);
	}

}
