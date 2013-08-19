package fr.openwide.core.showcase.web.application.portfolio.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.navigation.link.LinkFactory;
import fr.openwide.core.showcase.web.application.portfolio.component.UserProfilePanel;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.link.utils.CoreLinkParameterUtils;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.model.BindingModel;

public class UserDescriptionPage extends MainTemplate {
	private static final long serialVersionUID = -3229942018297644108L;
	
	private static final UserBinding USER = new UserBinding();
	
	@SpringBean
	private IUserService userService;
	
	public UserDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		IModel<User> userModel = CoreLinkParameterUtils.extractGenericEntityParameterModel(userService, parameters, Long.class);
		if (userModel.getObject() == null) {
			getSession().error(getString("common.error.noItem"));
			throw LinkFactory.get().userList().restartResponseException();
		}
		setDefaultModel(userModel);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("portfolio.pageTitle"), PortfolioMainPage.class));
		addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(userModel, USER.displayName()), UserDescriptionPage.class, parameters));
		
		add(new Label("pageTitle", BindingModel.of(userModel, USER.displayName())));
		
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
