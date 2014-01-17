package fr.openwide.core.showcase.web.application.portfolio.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.util.binding.Bindings;
import fr.openwide.core.showcase.web.application.portfolio.component.UserProfilePanel;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserDescriptionPage extends MainTemplate {
	private static final long serialVersionUID = -3229942018297644108L;
	
	public static IPageLinkDescriptor linkDescriptor(IModel<User> userModel) {
		return new LinkDescriptorBuilder()
				.page(UserDescriptionPage.class)
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.build();
	}
	
	public UserDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		setHeadPageTitleReversed(true);
		
		IModel<User> userModel = new GenericEntityModel<Long, User>(null);
		linkDescriptor(userModel).extractSafely(parameters, PortfolioMainPage.linkDescriptor());
		
		setDefaultModel(userModel);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("portfolio.pageTitle"), PortfolioMainPage.linkDescriptor()));
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().displayName()), UserDescriptionPage.linkDescriptor(userModel)));
		
		add(new Label("pageTitle", BindingModel.of(userModel, Bindings.user().displayName())));
		
		add(new UserProfilePanel("profilePanel", userModel));
	}
	
	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList();
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return PortfolioMainPage.class;
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
}
