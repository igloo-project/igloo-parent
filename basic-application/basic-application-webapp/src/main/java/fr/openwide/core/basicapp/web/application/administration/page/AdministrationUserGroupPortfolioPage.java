package fr.openwide.core.basicapp.web.application.administration.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationUserGroupPortfolioPage extends AdministrationTemplate {

	private static final long serialVersionUID = 2733071974944289365L;

	public AdministrationUserGroupPortfolioPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.usergroup"),
				AdministrationUserGroupPortfolioPage.class));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserGroupPortfolioPage.class;
	}
}
