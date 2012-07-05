package fr.openwide.core.wicket.more.console.maintenance.ehcache.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.console.maintenance.ehcache.component.EhCacheCachePortfolioPanel;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheManagerListModel;
import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;

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
