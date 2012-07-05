package fr.openwide.core.wicket.more.console.maintenance.search.page;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;

public class ConsoleMaintenanceSearchPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 2718354274888156322L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceSearchPage.class);
	
	@SpringBean(name="hibernateSearchService")
	protected IHibernateSearchService hibernateSearchService;
	
	public ConsoleMaintenanceSearchPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.maintenance.search");
		
		add(new Link<Void>("reindexContentLink") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				try {
					hibernateSearchService.reindexAll();
					getSession().success(getString("console.maintenance.search.reindex.success"));
				} catch(Exception e) {
					LOGGER.error("console.maintenance.search.reindex.failure", e);
					getSession().error(getString("console.maintenance.search.reindex.failure"));
				}
				setResponsePage(ConsoleMaintenanceSearchPage.class);
			}
		});
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceSearchPage.class;
	}

}
