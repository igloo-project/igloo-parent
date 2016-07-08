package fr.openwide.core.basicapp.web.application.common.typedescriptor.user;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationBasicUserDescriptionPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationBasicUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationTechnicalUserDescriptionPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationTechnicalUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserPortfolioTemplate;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.AbstractGenericEntityChildTypeDescriptor;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

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

	public IPageLinkGenerator description(IModel<U> userModel) {
		return description(userModel, new Model<Page>(null));
	}

	public IPageLinkDescriptor description(IModel<U> userModel, IModel<Page> sourcePageModel) {
		return LinkFactory.get().userDescription(descriptionPageClazz, userModel, typeDescriptor.getEntityClass(), sourcePageModel);
	}

	public IPageLinkDescriptor portfolio() {
		return LinkDescriptorBuilder.start().page(portfolioPageClazz);
	}

	public abstract U newInstance();

}
