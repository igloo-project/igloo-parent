package fr.openwide.core.basicapp.web.application.administration.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.administration.component.UserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationUserPortfolioPage extends AdministrationTemplate {

	private static final long serialVersionUID = 1824247169136460059L;

	@SpringBean
	private BasicApplicationConfigurer basicApplicationConfigurer;

	@SpringBean
	private IUserService userService;

	public AdministrationUserPortfolioPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user"),
				AdministrationUserPortfolioPage.class));
		
		add(new Label("pageTitle", new ResourceModel("administration.user.title")));
		
		IModel<List<User>> userListModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = -4518288683578265677L;
			
			@Override
			protected List<User> load() {
				return userService.list();
			}
		};
		
		add(new UserPortfolioPanel("portfolio", userListModel, basicApplicationConfigurer.getPortfolioItemsPerPage()));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserPortfolioPage.class;
	}
}
