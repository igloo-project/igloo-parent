package fr.openwide.core.basicapp.web.application.security.util;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.util.AbstractGenericEntityTypeDescriptor;
import fr.openwide.core.basicapp.web.application.security.password.page.SecurityPasswordExpirationPage;
import fr.openwide.core.basicapp.web.application.security.password.page.SecurityPasswordRecoveryPage;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.security.page.LoginSuccessPage;

public abstract class SecurityUserTypeDescriptor<U extends User> extends AbstractGenericEntityTypeDescriptor<SecurityUserTypeDescriptor<U>, U>{
	
	private static final long serialVersionUID = -1128901861897146296L;

	public static final SecurityUserTypeDescriptor<User> USER = new SecurityUserTypeDescriptor<User>(User.class, "user") {
		private static final long serialVersionUID = 1L;
		
		@Override
		public IPageLinkDescriptor signInPageLinkDescriptor() {
			return CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor();
		}
		
		@Override
		public IPageLinkDescriptor loginSuccessPageLinkDescriptor() {
			return LoginSuccessPage.linkDescriptor();
		}
		
		@Override
		public IPageLinkDescriptor passwordRecoveryPageLinkDescriptor() {
			return SecurityPasswordRecoveryPage.linkDescriptor();
		}
		
		@Override
		public IPageLinkDescriptor passwordExpirationPageLinkDescriptor() {
			return SecurityPasswordExpirationPage.linkDescriptor();
		}
		
		@Override
		protected Object readResolve() {
			return USER;
		}
	};

	private SecurityUserTypeDescriptor(Class<U> clazz, String name) {
		super(clazz, name);
	}

	public abstract IPageLinkDescriptor signInPageLinkDescriptor();

	public abstract IPageLinkDescriptor loginSuccessPageLinkDescriptor();

	public abstract IPageLinkDescriptor passwordRecoveryPageLinkDescriptor();

	public abstract IPageLinkDescriptor passwordExpirationPageLinkDescriptor();

}
