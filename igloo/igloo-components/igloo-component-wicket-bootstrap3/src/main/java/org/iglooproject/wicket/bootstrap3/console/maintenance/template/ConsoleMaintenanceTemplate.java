package org.iglooproject.wicket.bootstrap3.console.maintenance.template;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap3.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;

public abstract class ConsoleMaintenanceTemplate extends ConsoleTemplate {

	private static final long serialVersionUID = -3192604063259001201L;
	
	public ConsoleMaintenanceTemplate(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.maintenance");
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuSectionPageClass() {
		return ConsoleMaintenanceSearchPage.class;
	}
	
}
