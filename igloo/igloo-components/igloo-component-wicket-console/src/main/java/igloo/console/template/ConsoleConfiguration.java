package igloo.console.template;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItem;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItemRelatedPage;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.link.InvisibleLink;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import igloo.console.maintenance.authentication.page.ConsoleMaintenanceAuthenticationPage;
import igloo.console.maintenance.data.page.ConsoleMaintenanceDataPage;
import igloo.console.maintenance.file.page.ConsoleMaintenanceFilePage;
import igloo.console.maintenance.gestion.page.ConsoleMaintenanceGestionPage;
import igloo.console.maintenance.properties.page.ConsoleMaintenancePropertiesPage;
import igloo.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import igloo.console.maintenance.task.page.ConsoleMaintenanceTaskDetailPage;
import igloo.console.maintenance.task.page.ConsoleMaintenanceTaskListPage;
import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.markup.html.panel.InvisiblePanel;

public final class ConsoleConfiguration {
	
	private static final ConsoleConfiguration INSTANCE = new ConsoleConfiguration();
	
	private final List<ConsoleMenuSection> menuSections = Lists.newArrayList();
	
	private String baseUrl = null;
	
	private String consoleTitleKey;
	
	private Set<ResourceReference> cssResourcesReferences = Sets.newLinkedHashSet();
	
	private Set<ResourceReference> consoleAccessCssResourcesReferences = Sets.newLinkedHashSet();
	
	private IComponentFactory<Component> consoleAccessHeaderAdditionalContentComponentFactory = InvisiblePanel::new;
	
	private IComponentFactory<Component> consoleHeaderEnvironmentComponentFactory = InvisiblePanel::new;
	
	private IComponentFactory<Component> consoleHeaderAdditionalContentComponentFactory = InvisiblePanel::new;

	public static ConsoleConfiguration get() {
		if (!StringUtils.hasText(INSTANCE.baseUrl)) {
			throw new IllegalStateException("La console doit être initialisée par l'application avec une url de base.");
		}
		return INSTANCE;
	}
	
	public static ConsoleConfiguration build(String baseUrl, IPropertyService propertyService, ResourceSettings resourceSettings) {
		return build(baseUrl, "common.console", true, propertyService, resourceSettings);
	}
	
	public static ConsoleConfiguration build(
			String baseUrl,
			String consoleTitleKey,
			boolean buildDefault,
			IPropertyService propertyService,
			ResourceSettings resourceSettings
	) {
		INSTANCE.setBaseUrl(UrlUtils.normalizePath(baseUrl));
		INSTANCE.setConsoleTitleKey(consoleTitleKey);
		
		if (buildDefault) {
			ConsoleMenuSection maintenanceMenuSection = new ConsoleMenuSection("maintenanceMenuSection",
					"console.maintenance", "maintenance", ConsoleMaintenanceSearchPage.class);
			ConsoleMenuItem maintenanceSearchMenuItem = new ConsoleMenuItem("maintenanceSearchMenuItem",
					"console.maintenance.search", "search", ConsoleMaintenanceSearchPage.class);
			maintenanceMenuSection.addMenuItem(maintenanceSearchMenuItem);
			ConsoleMenuItem maintenanceGestionMenuItem = new ConsoleMenuItem("maintenanceGestionMenuItem",
					"console.maintenance.gestion", "gestion", ConsoleMaintenanceGestionPage.class);
			maintenanceMenuSection.addMenuItem(maintenanceGestionMenuItem);
			ConsoleMenuItem maintenanceDataMenuItem = new ConsoleMenuItem("maintenanceDataMenuItem",
					"console.maintenance.data", "data", ConsoleMaintenanceDataPage.class);
			maintenanceMenuSection.addMenuItem(maintenanceDataMenuItem);
			ConsoleMenuItem maintenancePropertiesMenuItem = new ConsoleMenuItem("maintenancePropertiesMenuItem",
					"console.maintenance.properties", "properties", ConsoleMaintenancePropertiesPage.class);
			maintenanceMenuSection.addMenuItem(maintenancePropertiesMenuItem);
			ConsoleMenuItem maintenanceAuthenticationMenuItem = new ConsoleMenuItem("maintenanceAuthenticationMenuItem",
					"console.maintenance.authentication", "authentication", ConsoleMaintenanceAuthenticationPage.class);
			maintenanceMenuSection.addMenuItem(maintenanceAuthenticationMenuItem);
			ConsoleMenuItem maintenanceTaskMenuItem = new ConsoleMenuItem("maintenanceTaskMenuItem",
					"console.maintenance.tasks", "task", ConsoleMaintenanceTaskListPage.class);
			ConsoleMenuItemRelatedPage maintenanceTaskDetailPage = new ConsoleMenuItemRelatedPage(
					"${" + CommonParameters.ID + "}/", ConsoleMaintenanceTaskDetailPage.class);
			maintenanceTaskMenuItem.addRelatedPage(maintenanceTaskDetailPage);
			maintenanceMenuSection.addMenuItem(maintenanceTaskMenuItem);
			ConsoleMenuItem maintenanceFileMenuItem = new ConsoleMenuItem("maintenanceFileMenuItem",
					"console.maintenance.file", "file", ConsoleMaintenanceFilePage.class);
			maintenanceMenuSection.addMenuItem(maintenanceFileMenuItem);
			
			INSTANCE.addMenuSection(maintenanceMenuSection);
		}
		INSTANCE.loadProviders(resourceSettings);
		
		return INSTANCE;
	}

	private void loadProviders(ResourceSettings resourceSettings) {
		ServiceLoader<IConsolePageProvider> loader = ServiceLoader.load(IConsolePageProvider.class);
		for (IConsolePageProvider pp : loader) {
			pp.install(this, resourceSettings);
		}
	}

	/**
	 * This method can be called only during startup phase.
	 * 
	 * @param newMenuItem menuItem to add
	 * @param sectionPredicate predicate to select section for insertion
	 * @param menuPredicate predicate to select menu for insertion (before or after item, see next argument)
	 * @param beforeMenuItem if true, <code>newMenuItem</code> is added before selected menu item; else after
	 * @param firstIfNotFound if true, and <code>menuPredicate</code> fails to find an entry, <code>newMenuItem</code>
	 * is added first in its section; else last
	 */
	public void insertMenu(ConsoleMenuItem newMenuItem,
			Predicate<ConsoleMenuSection> sectionPredicate,
			Predicate<ConsoleMenuItem> menuPredicate,
			boolean beforeMenuItem,
			boolean firstIfNotFound) { //NOSONAR
		ConsoleMenuSection sectionFound = null;
		boolean indexFound = false; 
		for (ConsoleMenuSection section : getMenuSections()) {
			if (sectionPredicate.test(section)) {
				sectionFound = section;
				int index = 0;
				for (ConsoleMenuItem menuItem : section.getMenuItems()) {
					if (menuPredicate.test(menuItem)) {
						index = beforeMenuItem ?
								section.getMenuItems().indexOf(menuItem) :
									section.getMenuItems().indexOf(menuItem) + 1;
						indexFound = true;
						// menu item is found
						break;
					}
				}
				if (!indexFound) {
					index = firstIfNotFound ? 0 : section.getMenuItems().size();
				}
				section.getMenuItems().add(index, newMenuItem);
				// no need to search other sections
				break;
			}
		}
		if (sectionFound == null) {
			throw new IllegalStateException("Section not found for insertion");
		}
	}
	
	public Link<Void> getConsoleLink(String wicketId) {
		if (!menuSections.isEmpty()) {
			return new BookmarkablePageLink<>(wicketId, menuSections.get(0).getPageClass());
		} else {
			return new InvisibleLink<>(wicketId);
		}
	}
	
	public void mountPages(WebApplication webApplication) {
		// First section is mounted behind the base url (ie. /console/)
		if (!menuSections.isEmpty()) {
			webApplication.mountPage(getBaseUrl() + "/", menuSections.get(0).getPageClass());
		}
		// Each section is mounted with its items
		for (ConsoleMenuSection menuSection : menuSections) {
			mountMenuSection(menuSection, webApplication);
		}
	}
	
	private void mountMenuSection(ConsoleMenuSection menuSection, WebApplication webApplication) {
		String menuSectionBaseUrl = getBaseUrl() + menuSection.getUrlFragment();
		webApplication.mountPage(menuSectionBaseUrl + "/", menuSection.getPageClass());
		for (ConsoleMenuItem menuItem : menuSection.getMenuItems()) {
			webApplication.mountPage(menuSectionBaseUrl + menuItem.getUrlFragment() + "/", menuItem.getPageClass());

			for (ConsoleMenuItemRelatedPage relatedPage : menuItem.getRelatedPages()) {
				webApplication.mountPage(menuSectionBaseUrl + menuItem.getUrlFragment() + relatedPage.getUrlFragment()
						+ "/", relatedPage.getPageClass());
			}
		}
	}
	
	public List<ConsoleMenuSection> getMenuSections() {
		return menuSections;
	}
	
	public void addMenuSection(ConsoleMenuSection menuSection) {
		menuSections.add(menuSection);
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getConsoleTitleKey() {
		return consoleTitleKey;
	}

	public void setConsoleTitleKey(String consoleTitleKey) {
		this.consoleTitleKey = consoleTitleKey;
	}

	public Set<ResourceReference> getCssResourcesReferences() {
		return cssResourcesReferences;
	}

	public boolean addCssResourceReference(ResourceReference cssResourceReference) {
		return cssResourcesReferences.add(cssResourceReference);
	}

	public Set<ResourceReference> getConsoleAccessCssResourcesReferences() {
		return consoleAccessCssResourcesReferences;
	}

	public boolean addConsoleAccessCssResourceReference(ResourceReference consoleAccessCssResourceReference) {
		return consoleAccessCssResourcesReferences.add(consoleAccessCssResourceReference);
	}

	public IComponentFactory<Component> getConsoleAccessHeaderAdditionalContentComponentFactory() {
		return consoleAccessHeaderAdditionalContentComponentFactory;
	}

	public void setConsoleAccessHeaderAdditionalContentComponentFactory(IComponentFactory<Component> consoleAccessHeaderAdditionalContentComponentFactory) {
		this.consoleAccessHeaderAdditionalContentComponentFactory = consoleAccessHeaderAdditionalContentComponentFactory;
	}

	public IComponentFactory<Component> getConsoleHeaderEnvironmentComponentFactory() {
		return consoleHeaderEnvironmentComponentFactory;
	}

	public void setConsoleHeaderEnvironmentComponentFactory(IComponentFactory<Component> consoleHeaderEnvironmentComponentFactory) {
		this.consoleHeaderEnvironmentComponentFactory = consoleHeaderEnvironmentComponentFactory;
	}

	public IComponentFactory<Component> getConsoleHeaderAdditionalContentComponentFactory() {
		return consoleHeaderAdditionalContentComponentFactory;
	}

	public void setConsoleHeaderAdditionalContentComponentFactory(IComponentFactory<Component> consoleHeaderAdditionalContentComponentFactory) {
		this.consoleHeaderAdditionalContentComponentFactory = consoleHeaderAdditionalContentComponentFactory;
	}

	private ConsoleConfiguration() {
	}
}
