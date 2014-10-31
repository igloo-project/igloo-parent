package fr.openwide.core.basicapp.web.application.navigation.util;

import java.io.Serializable;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;

public abstract class SignInUserTypeDescriptor<U extends User> implements Serializable {
	
	private static final long serialVersionUID = -1128901861897146296L;

	public static final SignInUserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new SignInUserTypeDescriptor<TechnicalUser>(TechnicalUser.class) {
		private static final long serialVersionUID = 1L;

		@Override
		public TechnicalUser newInstance() {
			return new TechnicalUser();
		}
		
		@Override
		protected Object readResolve() {
			return TECHNICAL_USER;
		}
	};
	
	public static final SignInUserTypeDescriptor<BasicUser> BASIC_USER = new SignInUserTypeDescriptor<BasicUser>(BasicUser.class) {
		private static final long serialVersionUID = 1L;

		@Override
		public BasicUser newInstance() {
			return new BasicUser();
		}
		
		@Override
		protected Object readResolve() {
			return BASIC_USER;
		}
	};

	
	private final Class<U> clazz;
	
	private SignInUserTypeDescriptor(Class<U> clazz) {
		this.clazz = clazz;
	}

	public Class<U> getUserClass() {
		return clazz;
	}
	
	public abstract U newInstance();
	
	protected abstract Object readResolve();

}
