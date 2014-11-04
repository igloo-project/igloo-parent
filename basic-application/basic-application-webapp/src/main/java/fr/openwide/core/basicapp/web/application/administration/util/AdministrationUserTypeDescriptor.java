package fr.openwide.core.basicapp.web.application.administration.util;

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
import fr.openwide.core.basicapp.web.application.common.util.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class AdministrationUserTypeDescriptor<U extends User> extends UserTypeDescriptor<U> {
	
	private static final long serialVersionUID = -1128901861897146296L;

	public static final AdministrationUserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new AdministrationUserTypeDescriptor<TechnicalUser>(
			TechnicalUser.class, "technicalUser", AdministrationTechnicalUserDescriptionPage.class,
			AdministrationTechnicalUserPortfolioPage.class) {
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
	
	public static final AdministrationUserTypeDescriptor<BasicUser> BASIC_USER = new AdministrationUserTypeDescriptor<BasicUser>(
			BasicUser.class, "basicUser", AdministrationBasicUserDescriptionPage.class,
			AdministrationBasicUserPortfolioPage.class) {
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

	private final Class<? extends AdministrationUserDescriptionTemplate<U>> fichePageClazz;
	
	private final Class<? extends AdministrationUserPortfolioTemplate<U>> listePageClazz;
	
	private AdministrationUserTypeDescriptor(Class<U> clazz, String name,
			Class<? extends AdministrationUserDescriptionTemplate<U>> fichePageClazz,
			Class<? extends AdministrationUserPortfolioTemplate<U>> listePageClazz) {
		super(clazz, name);
		this.fichePageClazz = fichePageClazz;
		this.listePageClazz = listePageClazz;
	}

	public Class<? extends AdministrationUserDescriptionTemplate<U>> getFicheClass() {
		return fichePageClazz;
	}

	public Class<? extends AdministrationUserPortfolioTemplate<U>> getListeClass() {
		return listePageClazz;
	}
	
	public IPageLinkGenerator fiche(IModel<U> userModel) {
		return fiche(userModel, new Model<Page>(null));
	}
	
	public IPageLinkDescriptor fiche(IModel<U> userModel, IModel<Page> sourcePageModel) {
		return LinkFactory.get().ficheUser(fichePageClazz, userModel, getEntityClass(), sourcePageModel);
	}
	
	public IPageLinkDescriptor liste() {
		return new LinkDescriptorBuilder().page(listePageClazz).build();
	}
	
	public abstract U newInstance();

}
