package org.iglooproject.wicket.more.console.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.UrlUtils;

public class ConsoleMenuSection implements Serializable {
	
	private static final long serialVersionUID = 3035535046310049094L;
	
	private String name;
	
	private String displayStringKey;
	
	private String urlFragment;
	
	private Class<? extends WebPage> pageClass;
	
	private List<ConsoleMenuItem> menuItems;
	
	public ConsoleMenuSection(String name, String displayStringKey, String urlFragment, Class<? extends WebPage> pageClass) {
		this.name = name;
		this.displayStringKey = displayStringKey;
		this.urlFragment = UrlUtils.normalizePath(urlFragment);
		this.pageClass = pageClass;
		this.menuItems = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayStringKey() {
		return displayStringKey;
	}
	
	public void setDisplayStringKey(String displayStringKey) {
		this.displayStringKey = displayStringKey;
	}

	public String getUrlFragment() {
		return urlFragment;
	}
	
	public void setUrlFragment(String urlFragment) {
		this.urlFragment = urlFragment;
	}
	
	public Class<? extends WebPage> getPageClass() {
		return pageClass;
	}
	
	public void setPageClass(Class<? extends WebPage> pageClass) {
		this.pageClass = pageClass;
	}
	
	public List<ConsoleMenuItem> getMenuItems() {
		return menuItems;
	}
	
	public void setMenuItems(List<ConsoleMenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public void addMenuItem(ConsoleMenuItem menuItem) {
		menuItems.add(menuItem);
	}
}
