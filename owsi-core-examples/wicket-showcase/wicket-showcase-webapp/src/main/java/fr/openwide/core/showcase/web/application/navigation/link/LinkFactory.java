package fr.openwide.core.showcase.web.application.navigation.link;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.util.binding.Binding;
import fr.openwide.core.showcase.web.application.navigation.page.SignInPage;
import fr.openwide.core.showcase.web.application.portfolio.page.PortfolioMainPage;
import fr.openwide.core.showcase.web.application.portfolio.page.UserDescriptionPage;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.factory.CoreWicketAuthenticatedApplicationLinkFactory;
import fr.openwide.core.wicket.more.link.utils.CoreLinkParameterUtils;
import fr.openwide.core.wicket.more.model.BindingModel;

public final class LinkFactory extends CoreWicketAuthenticatedApplicationLinkFactory {
	
	private static final LinkFactory INSTANCE = new LinkFactory();
	
	private LinkFactory() { }
	
	public static LinkFactory get() {
		return INSTANCE;
	}

	@Override
	public IPageLinkDescriptor signIn() {
		return builder()
				.page(SignInPage.class)
				.build();
	}
	
	public IPageLinkDescriptor userList() {
		return builder()
				.page(PortfolioMainPage.class)
				.build();
	}
	
	public IPageLinkDescriptor userDescription(IModel<? extends User> userModel) {
		return builder()
				.page(UserDescriptionPage.class)
				.parameter(CoreLinkParameterUtils.ID_PARAMETER, userModel).mandatory()
				.build();
	}
	
	public IPageLinkDescriptor linksTest(IModel<? extends Class<? extends Page>> pageModel, IModel<? extends User> userModel) {
		return builder()
				.page(pageModel)
				.parameter(CoreLinkParameterUtils.ID_PARAMETER, BindingModel.of(userModel, Binding.user().id())).mandatory()
				.build();
	}
}
