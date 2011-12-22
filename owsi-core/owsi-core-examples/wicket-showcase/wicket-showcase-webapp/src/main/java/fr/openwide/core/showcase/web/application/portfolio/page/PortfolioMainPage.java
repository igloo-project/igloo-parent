package fr.openwide.core.showcase.web.application.portfolio.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.core.util.spring.ShowcaseConfigurer;
import fr.openwide.core.showcase.web.application.portfolio.component.UserPortfolioPanel;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class PortfolioMainPage extends MainTemplate {
	private static final long serialVersionUID = 6572019030268485555L;
	
	@SpringBean
	private ShowcaseConfigurer showcaseConfigurer;
	
	@SpringBean
	private IUserService userService;
	
	public PortfolioMainPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement("portfolio.pageTitle"));
		
		add(new Label("pageTitle", new ResourceModel("portfolio.pageTitle")));
		
		// TODO JGO - implémenter la recherche quand y'aura le temps
		IModel<List<User>> userListModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = -5757718865027562782L;
			
			@Override
			protected List<User> load() {
				return userService.list();
			}
		};
		
		add(new UserPortfolioPanel("userPortfolio", userListModel, showcaseConfigurer.getPortfolioItemsPerPage()));
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
