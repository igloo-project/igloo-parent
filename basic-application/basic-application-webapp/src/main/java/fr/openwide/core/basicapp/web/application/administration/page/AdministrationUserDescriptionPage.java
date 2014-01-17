package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.component.UserMembershipsPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserProfilePanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.link.model.PageModel;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AdministrationUserDescriptionPage extends AdministrationTemplate {

	private static final long serialVersionUID = -550100874222819991L;
	
	public static IPageLinkDescriptor linkDescriptor(IModel<User> userModel, IModel<Page> sourcePageModel) {
		return new LinkDescriptorBuilder()
				.page(AdministrationUserDescriptionPage.class)
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.map(CommonParameters.SOURCE_PAGE_ID, sourcePageModel, Page.class).optional()
				.build();
	}
	
	public static IPageLinkGenerator linkGenerator(IModel<User> userModel) {
		return linkDescriptor(userModel, Model.of((Page)null));
	}

	public AdministrationUserDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		IModel<User> userModel = new GenericEntityModel<Long, User>(null);
		
		IModel<Page> sourcePageModel = new PageModel<Page>();
		
		linkDescriptor(userModel, sourcePageModel).extractSafely(parameters, AdministrationUserPortfolioPage.linkDescriptor(),
				getString("administration.user.error"));
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user"),
				AdministrationUserPortfolioPage.linkDescriptor()));
		
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName()),
				AdministrationUserDescriptionPage.linkDescriptor(userModel, sourcePageModel)));
		
		add(new Label("pageTitle", BindingModel.of(userModel, Bindings.user().fullName())));
		
		Component backToSourcePage = LinkFactory.get().linkGenerator(sourcePageModel, AdministrationUserPortfolioPage.class)
				.link("backToSourcePage").setAutoHideIfInvalid(true);
		add(
				backToSourcePage,
				AdministrationUserPortfolioPage.linkDescriptor().link("backToList")
						.add(new PlaceholderBehavior().component(backToSourcePage))
		);
		
		add(new UserProfilePanel("profile", userModel));
		add(new UserMembershipsPanel("groups", userModel));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserPortfolioPage.class;
	}
}
