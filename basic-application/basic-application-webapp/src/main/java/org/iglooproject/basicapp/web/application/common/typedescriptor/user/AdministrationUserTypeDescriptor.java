package org.iglooproject.basicapp.web.application.common.typedescriptor.user;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDescriptionPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserPortfolioPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationTechnicalUserDescriptionPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationTechnicalUserPortfolioPage;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserPortfolioTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.AbstractGenericEntityChildTypeDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public abstract class AdministrationUserTypeDescriptor<U extends User> extends
		AbstractGenericEntityChildTypeDescriptor<UserTypeDescriptor<U>, U> {

	private static final long serialVersionUID = -1128901861897146296L;

	public static final AdministrationUserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new AdministrationUserTypeDescriptor<TechnicalUser>(
			UserTypeDescriptor.TECHNICAL_USER, AdministrationTechnicalUserDescriptionPage.class,
			AdministrationTechnicalUserPortfolioPage.class) {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return TECHNICAL_USER;
		}
		
		@Override
		public TechnicalUser newInstance() {
			return new TechnicalUser();
		}
	};

	public static final AdministrationUserTypeDescriptor<BasicUser> BASIC_USER = new AdministrationUserTypeDescriptor<BasicUser>(
			UserTypeDescriptor.BASIC_USER, AdministrationBasicUserDescriptionPage.class,
			AdministrationBasicUserPortfolioPage.class) {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return BASIC_USER;
		}
		
		@Override
		public BasicUser newInstance() {
			return new BasicUser();
		}
	};

	private final Class<? extends AdministrationUserDescriptionTemplate<U>> descriptionPageClazz;

	private final Class<? extends AdministrationUserPortfolioTemplate<U>> portfolioPageClazz;

	private AdministrationUserTypeDescriptor(UserTypeDescriptor<U> typeDescriptor,
			Class<? extends AdministrationUserDescriptionTemplate<U>> descriptionPageClazz,
			Class<? extends AdministrationUserPortfolioTemplate<U>> portfolioPageClazz) {
		super(typeDescriptor);
		this.descriptionPageClazz = descriptionPageClazz;
		this.portfolioPageClazz = portfolioPageClazz;
	}

	public Class<? extends AdministrationUserDescriptionTemplate<U>> getDescriptionClass() {
		return descriptionPageClazz;
	}

	public Class<? extends AdministrationUserPortfolioTemplate<U>> getPortfolioClass() {
		return portfolioPageClazz;
	}

	public IPageLinkDescriptor portfolio() {
		return LinkDescriptorBuilder.start().page(portfolioPageClazz);
	}

	public abstract U newInstance();

}
