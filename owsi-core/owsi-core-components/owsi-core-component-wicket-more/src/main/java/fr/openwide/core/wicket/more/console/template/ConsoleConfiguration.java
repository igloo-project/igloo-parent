package fr.openwide.core.wicket.more.console.template;

import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.resource.ResourceReference;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuItem;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuItemRelatedPage;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuSection;
import fr.openwide.core.wicket.more.console.maintenance.authentication.page.ConsoleMaintenanceAuthenticationPage;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.page.ConsoleMaintenanceEhCachePage;
import fr.openwide.core.wicket.more.console.maintenance.file.page.ConsoleMaintenanceFilePage;
import fr.openwide.core.wicket.more.console.maintenance.gestion.page.ConsoleMaintenanceGestionPage;
import fr.openwide.core.wicket.more.console.maintenance.infinispan.page.ConsoleMaintenanceInfinispanPage;
import fr.openwide.core.wicket.more.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import fr.openwide.core.wicket.more.console.maintenance.task.page.ConsoleMaintenanceTaskDescriptionPage;
import fr.openwide.core.wicket.more.console.maintenance.task.page.ConsoleMaintenanceTaskListPage;
import fr.openwide.core.wicket.more.console.maintenance.upgrade.page.ConsoleMaintenanceDonneesPage;
import fr.openwide.core.wicket.more.console.template.style.ConsoleLessCssResourceReference;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.link.InvisibleLink;

public final class ConsoleConfiguration {
	
	private static final ConsoleConfiguration INSTANCE = new ConsoleConfiguration();
	
	private final List<ConsoleMenuSection> menuSections = Lists.newArrayList();
	
	private String baseUrl = null;
	
	private String consolePageTitleKey;
	
	private Set<ResourceReference> cssResourcesReferences = Sets.newLinkedHashSet();
	
	public static ConsoleConfiguration get() {
		if (!StringUtils.hasText(INSTANCE.baseUrl)) {
			throw new IllegalStateException("La console doit être initialisée par l'application avec une url de base.");
		}
		return INSTANCE;
	}
	
	public static ConsoleConfiguration build(String baseUrl) {
		return build(baseUrl, "console.pageTitle", true);
	}
	
	public static ConsoleConfiguration build(String baseUrl, String consolePageTitleKey, boolean buildDefault) {
		INSTANCE.setBaseUrl(UrlUtils.normalizePath(baseUrl));
		INSTANCE.setConsolePageTitleKey(consolePageTitleKey);
		
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
			ConsoleMenuItem infinispanMenuItem = new ConsoleMenuItem("infinispanMenuItem",
					"console.maintenance.infinispan", "infinispan", ConsoleMaintenanceInfinispanPage.class);
			maintenanceMenuSection.addMenuItem(infinispanMenuItem);
			
			INSTANCE.addMenuSection(maintenanceMenuSection);
			INSTANCE.addCssResourceReference(ConsoleLessCssResourceReference.get());
		}
		
		return INSTANCE;
	}
	
	public Link<Void> getConsoleLink(String wicketId) {
		if (!menuSections.isEmpty()) {
			return new BookmarkablePageLink<Void>(wicketId, menuSections.get(0).getPageClass());
		} else {
			return new InvisibleLink<Void>(wicketId);
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

	public String getConsolePageTitleKey() {
		return consolePageTitleKey;
	}

	public void setConsolePageTitleKey(String consolePageTitleKey) {
		this.consolePageTitleKey = consolePageTitleKey;
	}

	public Set<ResourceReference> getCssResourcesReferences() {
		return cssResourcesReferences;
	}

	public boolean addCssResourceReference(ResourceReference cssResourceReference) {
		return cssResourcesReferences.add(cssResourceReference);
	}

	private ConsoleConfiguration() {
	}
}
