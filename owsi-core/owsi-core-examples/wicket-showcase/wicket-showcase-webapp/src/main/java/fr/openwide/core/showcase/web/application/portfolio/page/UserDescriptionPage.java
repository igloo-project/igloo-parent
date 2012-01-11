package fr.openwide.core.showcase.web.application.portfolio.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.portfolio.component.UserIdentityCardPanel;
import fr.openwide.core.showcase.web.application.util.LinkUtils;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserDescriptionPage extends MainTemplate {
	private static final long serialVersionUID = -3229942018297644108L;
	
	@SpringBean
	private IUserService userService;
	
	public UserDescriptionPage(PageParameters parameters) {
		super(parameters);
		
		User user = null;
		try {
			user = userService.getById(parameters.get(LinkUtils.ITEM_ID_PARAMETER).toInteger());
		} catch (Exception e) {
			getSession().error(getString("common.error.noItem"));
			redirect(PortfolioMainPage.class);
			return;
		}
		IModel<User> userModel = new GenericEntityModel<Integer, User>(user);
		setDefaultModel(userModel);
		
		add(new UserIdentityCardPanel("descriptionPanel", userModel));
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
