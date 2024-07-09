package basicapp.front.administration.template;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

import basicapp.front.administration.page.AdministrationBasicUserListPage;
import basicapp.front.common.template.MainTemplate;

@AuthorizeInstantiation(CoreAuthorityConstants.ROLE_ADMIN)
public abstract class AdministrationTemplate extends MainTemplate {

	private static final long serialVersionUID = -5571981353426833725L;

	public AdministrationTemplate(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("navigation.administration")
		));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return AdministrationBasicUserListPage.class;
	}

	@Override
	protected abstract Class<? extends WebPage> getSecondMenuPage();

}
