package org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.component.EhCacheCachePortfolioPanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.more.console.maintenance.ehcache.model.EhCacheCacheManagerListModel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class ConsoleMaintenanceEhCachePage extends ConsoleMaintenanceTemplate {
	
	private static final long serialVersionUID = -7061578100018864443L;
	
	public ConsoleMaintenanceEhCachePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.ehcache")));
		
		add(new EhCacheCachePortfolioPanel("portfolio", new EhCacheCacheManagerListModel()));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceEhCachePage.class;
	}

}
