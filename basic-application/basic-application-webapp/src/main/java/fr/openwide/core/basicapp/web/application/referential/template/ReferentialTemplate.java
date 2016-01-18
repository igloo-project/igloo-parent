package fr.openwide.core.basicapp.web.application.referential.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.common.template.MainTemplate;
import fr.openwide.core.basicapp.web.application.referential.page.ReferentialPage;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

//@AuthorizeInstantiation(CoreAuthorityConstants.ROLE_ADMIN)
public abstract class ReferentialTemplate extends MainTemplate {

	private static final long serialVersionUID = -5226976873952135450L;

	protected final IModel<String> pageTitleModel;
	
	public ReferentialTemplate(PageParameters parameters) {
		super(parameters);
		
		pageTitleModel = new ResourceModel("navigation.referential");

		addBreadCrumbElement(new BreadCrumbElement(pageTitleModel));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return ReferentialPage.class;
	}

	@Override
	protected abstract Class<? extends WebPage> getSecondMenuPage();
}
