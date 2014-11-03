package fr.openwide.core.basicapp.web.application.security.login.util;

import java.io.Serializable;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.security.page.LoginSuccessPage;

public abstract class SignInUserTypeDescriptor<U extends User> implements Serializable {
	
	private static final long serialVersionUID = -1128901861897146296L;

	public static final SignInUserTypeDescriptor<User> DEFAULT = new SignInUserTypeDescriptor<User>(User.class) {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return DEFAULT;
		}

		@Override
		public IPageLinkDescriptor signInPageLinkDescriptor() {
			return CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor();
		}

		@Override
		public IPageLinkDescriptor loginSuccessPageLinkDescriptor() {
			return LoginSuccessPage.linkDescriptor();
		}
	};

	private final Class<U> clazz;
	
	private SignInUserTypeDescriptor(Class<U> clazz) {
		this.clazz = clazz;
	}

	public Class<U> getUserClass() {
		return clazz;
	}

	public abstract IPageLinkDescriptor signInPageLinkDescriptor();

	public abstract IPageLinkDescriptor loginSuccessPageLinkDescriptor();

	protected abstract Object readResolve();

}
