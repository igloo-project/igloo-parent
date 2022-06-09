package igloo.console.maintenance.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

import igloo.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import igloo.console.template.ConsoleTemplate;

public abstract class ConsoleMaintenanceTemplate extends ConsoleTemplate {

	private static final long serialVersionUID = -3192604063259001201L;

	public ConsoleMaintenanceTemplate(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("console.maintenance"),
			ConsoleMaintenanceSearchPage.linkDescriptor()
		));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return ConsoleMaintenanceSearchPage.class;
	}

}
