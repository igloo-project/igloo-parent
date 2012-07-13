package fr.openwide.core.basicapp.web.application.administration.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.administration.component.UserGroupPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationUserGroupPortfolioPage extends AdministrationTemplate {

	private static final long serialVersionUID = 2733071974944289365L;

	@SpringBean
	private BasicApplicationConfigurer configurer;

	@SpringBean
	private IUserGroupService userGroupService;

	public AdministrationUserGroupPortfolioPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.usergroup"),
				AdministrationUserGroupPortfolioPage.class));
		
		add(new Label("pageTitle", new ResourceModel("administration.usergroup.title")));
		
		IModel<List<UserGroup>> userGroupListModel = new LoadableDetachableModel<List<UserGroup>>() {
			private static final long serialVersionUID = -4518288683578265677L;
			
			@Override
			protected List<UserGroup> load() {
				return userGroupService.list();
			}
		};
		
		add(new UserGroupPortfolioPanel("portfolio", userGroupListModel, configurer.getPortfolioItemsPerPage()));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserGroupPortfolioPage.class;
	}
}
