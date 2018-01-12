package org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.component.EhCacheCachePortfolioPanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap4.console.template.ConsoleTemplate;
import org.iglooproject.wicket.more.console.maintenance.ehcache.model.EhCacheCacheManagerListModel;

public class ConsoleMaintenanceEhCachePage extends ConsoleMaintenanceTemplate {
	
	private static final long serialVersionUID = -7061578100018864443L;
	
	public ConsoleMaintenanceEhCachePage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.maintenance.ehcache");
		
		add(new EhCacheCachePortfolioPanel("portfolio", new EhCacheCacheManagerListModel()));
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceEhCachePage.class;
	}

}
