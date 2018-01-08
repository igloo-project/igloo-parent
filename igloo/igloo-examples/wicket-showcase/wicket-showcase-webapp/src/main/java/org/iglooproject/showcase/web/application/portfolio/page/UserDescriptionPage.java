package org.iglooproject.showcase.web.application.portfolio.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.util.binding.Bindings;
import org.iglooproject.showcase.web.application.portfolio.component.UserProfilePanel;
import org.iglooproject.showcase.web.application.util.template.MainTemplate;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class UserDescriptionPage extends MainTemplate {

	private static final long serialVersionUID = -3229942018297644108L;

	public static final ILinkDescriptorMapper<IPageLinkDescriptor, IModel<User>> MAPPER =
			LinkDescriptorBuilder.start()
					.model(User.class)
					.page(UserDescriptionPage.class);
	
	public UserDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		setHeadPageTitleReversed(true);
		
		IModel<User> userModel = new GenericEntityModel<Long, User>(null);
		MAPPER.map(userModel).extractSafely(parameters, PortfolioMainPage.linkDescriptor());
		
		setDefaultModel(userModel);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("portfolio.pageTitle"), PortfolioMainPage.linkDescriptor()));
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().displayName()), UserDescriptionPage.MAPPER.map((userModel))));
		
		add(new Label("pageTitle", BindingModel.of(userModel, Bindings.user().displayName())));
		
		add(new UserProfilePanel("profilePanel", userModel));
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
