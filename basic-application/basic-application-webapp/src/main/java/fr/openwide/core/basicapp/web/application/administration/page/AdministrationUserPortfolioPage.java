package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.administration.component.AdministrationUserSearchPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.form.UserFormPopupPanel;
import fr.openwide.core.basicapp.web.application.administration.model.UserDataProvider;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationUserPortfolioPage extends AdministrationTemplate {

	private static final long serialVersionUID = 1824247169136460059L;

	@SpringBean
	private BasicApplicationConfigurer basicApplicationConfigurer;

	public AdministrationUserPortfolioPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.user"),
				AdministrationUserPortfolioPage.class));
		
		IModel<String> searchTermModel = Model.of("");
		
		UserPortfolioPanel portfolioPanel = new UserPortfolioPanel("portfolio", new UserDataProvider(searchTermModel),
				basicApplicationConfigurer.getPortfolioItemsPerPage());
		add(portfolioPanel);

		add(new AdministrationUserSearchPanel("searchPanel", portfolioPanel.getPageable(), searchTermModel));
		
		// User create popup
		UserFormPopupPanel userCreatePanel = new UserFormPopupPanel("userCreatePopupPanel");
		add(userCreatePanel);
		
		Button createUser = new Button("createUser");
		createUser.add(new AjaxModalOpenBehavior(userCreatePanel, MouseEvent.CLICK) {
			private static final long serialVersionUID = 5414159291353181776L;
			
			@Override
			protected void onShow(AjaxRequestTarget target) {
			}
		});
		add(createUser);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserPortfolioPage.class;
	}
}
