package fr.openwide.core.basicapp.web.application.administration.util;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationBasicUserDescriptionPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationBasicUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationTechnicalUserDescriptionPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationTechnicalUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserDescriptionTemplate;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationUserPortfolioTemplate;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class AdministrationUserTypeDescriptor<U extends User> implements Serializable {
	
	private static final long serialVersionUID = -1128901861897146296L;

	private static final Collection<AdministrationUserTypeDescriptor<?>> ALL = Lists.newArrayList();
	
	@SuppressWarnings("unchecked")
	public static final <U extends User> AdministrationUserTypeDescriptor<? extends U> get(U user) {
		if (user == null) {
			return null;
		}
		
		for (AdministrationUserTypeDescriptor<?> type : ALL) {
			if (type.getUserClass().isInstance(user)) {
				return (AdministrationUserTypeDescriptor<? extends U>) type;
			}
		}
		
		throw new IllegalStateException("Unknown type for user " + user);
	}
	
	public static final AdministrationUserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new AdministrationUserTypeDescriptor<TechnicalUser>(TechnicalUser.class,
			AdministrationTechnicalUserDescriptionPage.class, AdministrationTechnicalUserPortfolioPage.class) {
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
	
	public static final AdministrationUserTypeDescriptor<BasicUser> BASIC_USER = new AdministrationUserTypeDescriptor<BasicUser>(BasicUser.class,
			AdministrationBasicUserDescriptionPage.class, AdministrationBasicUserPortfolioPage.class) {
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
	
	private final Class<? extends AdministrationUserDescriptionTemplate<U>> fichePageClazz;
	
	private final Class<? extends AdministrationUserPortfolioTemplate<U>> listePageClazz;
	
	private AdministrationUserTypeDescriptor(Class<U> clazz,
			Class<? extends AdministrationUserDescriptionTemplate<U>> fichePageClazz,
			Class<? extends AdministrationUserPortfolioTemplate<U>> listePageClazz) {
		ALL.add(this);
		this.clazz = clazz;
		this.fichePageClazz = fichePageClazz;
		this.listePageClazz = listePageClazz;
	}

	public Class<U> getUserClass() {
		return clazz;
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
		return LinkFactory.get().ficheUser(fichePageClazz, userModel, clazz, sourcePageModel);
	}
	
	public IPageLinkDescriptor liste() {
		return new LinkDescriptorBuilder().page(listePageClazz).build();
	}
	
	public abstract U newInstance();
	
	protected abstract Object readResolve();

}
