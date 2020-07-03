package org.iglooproject.wicket.bootstrap4.console.template;

import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.bootstrap4.console.maintenance.authentication.page.ConsoleMaintenanceAuthenticationPage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.page.ConsoleMaintenanceEhCachePage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.file.page.ConsoleMaintenanceFilePage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.gestion.page.ConsoleMaintenanceGestionPage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.task.page.ConsoleMaintenanceTaskDescriptionPage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.task.page.ConsoleMaintenanceTaskListPage;
import org.iglooproject.wicket.bootstrap4.console.maintenance.upgrade.page.ConsoleMaintenanceDonneesPage;
import org.iglooproject.wicket.markup.html.panel.InvisiblePanel;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItem;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItemRelatedPage;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.factory.AbstractComponentFactory;
import org.iglooproject.wicket.more.markup.html.factory.IComponentFactory;
import org.iglooproject.wicket.more.markup.html.link.InvisibleLink;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class ConsoleConfiguration {
	
	private static final ConsoleConfiguration INSTANCE = new ConsoleConfiguration();
	
	private final List<ConsoleMenuSection> menuSections = Lists.newArrayList();
	
	private String baseUrl = null;
	
	private String consoleTitleKey;
	
	private Set<ResourceReference> cssResourcesReferences = Sets.newLinkedHashSet();
	
	private Set<ResourceReference> consoleAccessCssResourcesReferences = Sets.newLinkedHashSet();
	
	private IComponentFactory<Component> consoleAccessHeaderAdditionalContentComponentFactory = new AbstractComponentFactory<Component>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Component create(String wicketId) {
			return new InvisiblePanel(wicketId);
		}
	};
	
	private IComponentFactory<Component> consoleHeaderEnvironmentComponentFactory = new AbstractComponentFactory<Component>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Component create(String wicketId) {
			return new InvisiblePanel(wicketId);
		}
	};
	
	private IComponentFactory<Component> consoleHeaderAdditionalContentComponentFactory = new AbstractComponentFactory<Component>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Component create(String wicketId) {
			return new InvisiblePanel(wicketId);
		}
	};
	
	public static ConsoleConfiguration get() {
		if (!StringUtils.hasText(INSTANCE.baseUrl)) {
			throw new IllegalStateException("La console doit être initialisée par l'application avec une url de base.");
		}
		return INSTANCE;
	}
	
	public static ConsoleConfiguration build(String baseUrl, IPropertyService propertyService) {
		return build(baseUrl, "common.console", true, propertyService);
	}
	
	public static ConsoleConfiguration build(
			String baseUrl,
			String consoleTitleKey,
			boolean buildDefault,
			IPropertyService propertyService
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
			ConsoleMenuItem maintenanceEhcacheMenuItem = new ConsoleMenuItem("maintenanceEhcacheMenuItem",
					"console.maintenance.ehcache", "ehcache", ConsoleMaintenanceEhCachePage.class);
			maintenanceMenuSection.addMenuItem(maintenanceEhcacheMenuItem);
			ConsoleMenuItem maintenanceDonneesMenuItem = new ConsoleMenuItem("maintenanceDonneesMenuItem",
					"console.maintenance.donnees", "donnees", ConsoleMaintenanceDonneesPage.class);
			maintenanceMenuSection.addMenuItem(maintenanceDonneesMenuItem);
			ConsoleMenuItem authenticationMenuItem = new ConsoleMenuItem("signInAsMenuItem",
					"console.maintenance.authentication", "authentication", ConsoleMaintenanceAuthenticationPage.class);
			maintenanceMenuSection.addMenuItem(authenticationMenuItem);
			ConsoleMenuItem maintenanceTasksMenuItem = new ConsoleMenuItem("maintenanceTasksMenuItem",
					"console.maintenance.tasks", "tasks", ConsoleMaintenanceTaskListPage.class);
			ConsoleMenuItemRelatedPage maintenanceTaskDetailsPage = new ConsoleMenuItemRelatedPage(
					"${" + CommonParameters.ID + "}/", ConsoleMaintenanceTaskDescriptionPage.class);
			maintenanceTasksMenuItem.addRelatedPage(maintenanceTaskDetailsPage);
			maintenanceMenuSection.addMenuItem(maintenanceTasksMenuItem);
			ConsoleMenuItem fileMenuItem = new ConsoleMenuItem("fileMenuItem",
					"console.maintenance.file", "file", ConsoleMaintenanceFilePage.class);
			maintenanceMenuSection.addMenuItem(fileMenuItem);
			
			INSTANCE.addMenuSection(maintenanceMenuSection);
		}
		
		return INSTANCE;
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
