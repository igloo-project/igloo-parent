package fr.openwide.core.basicapp.web.application.common.typedescriptor.user;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.AbstractGenericEntityChildTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.util.ResourceKeyGenerator;
import fr.openwide.core.basicapp.web.application.security.password.page.SecurityPasswordCreationPage;
import fr.openwide.core.basicapp.web.application.security.password.page.SecurityPasswordExpirationPage;
import fr.openwide.core.basicapp.web.application.security.password.page.SecurityPasswordRecoveryPage;
import fr.openwide.core.basicapp.web.application.security.password.page.SecurityPasswordResetPage;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.security.page.LoginSuccessPage;

public abstract class SecurityUserTypeDescriptor<U extends User> extends
		AbstractGenericEntityChildTypeDescriptor<UserTypeDescriptor<U>, U> {

	private static final long serialVersionUID = -1128901861897146296L;

	public static final SecurityUserTypeDescriptor<? extends User> USER = new SecurityUserTypeDescriptor<User>(UserTypeDescriptor.USER) {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return USER;
		}
		
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
		public ITwoParameterLinkDescriptorMapper<? extends ILinkGenerator, User, String>
				passwordResetPageLinkDescriptorMapper() {
			return SecurityPasswordResetPage.MAPPER;
		}

		@Override
		public ITwoParameterLinkDescriptorMapper<? extends ILinkGenerator, User, String>
				passwordCreationPageLinkDescriptorMapper() {
			return SecurityPasswordCreationPage.MAPPER;
		}
	};

	private SecurityUserTypeDescriptor(UserTypeDescriptor<U> typeDescriptor) {
		super(typeDescriptor);
	}

	public abstract IPageLinkDescriptor signInPageLinkDescriptor();

	public abstract IPageLinkDescriptor loginSuccessPageLinkDescriptor();

	public abstract IPageLinkDescriptor passwordRecoveryPageLinkDescriptor();

	public abstract IPageLinkDescriptor passwordExpirationPageLinkDescriptor();

	public abstract ITwoParameterLinkDescriptorMapper<? extends ILinkGenerator, User, String>
			passwordResetPageLinkDescriptorMapper();

	public abstract ITwoParameterLinkDescriptorMapper<? extends ILinkGenerator, User, String>
			passwordCreationPageLinkDescriptorMapper();
	
	public ResourceKeyGenerator resourceKeyGenerator() {
		return typeDescriptor.resourceKeyGenerator().withPrefix("security");
	}

}
