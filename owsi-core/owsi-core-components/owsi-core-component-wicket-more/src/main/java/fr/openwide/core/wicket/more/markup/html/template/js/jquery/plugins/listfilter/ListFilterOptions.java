package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter;

import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

public class ListFilterOptions extends Options {
	private static final long serialVersionUID = 8361789161890761478L;

	private String containerClass;

	private String inputSelector;

	private String itemsSelector;

	private String scanSelector;

	private String hiddenElementClass;

	private String emptyListClass;

	private Boolean enabled;

	private String disabledClass;

	@Override
	public CharSequence getJavaScriptOptions() {
		if (containerClass != null) {
			put("containerClass", JsUtils.quotes(containerClass));
		}
		if (inputSelector != null) {
			put("inputSelector", JsUtils.quotes(inputSelector));
		}
		if (itemsSelector != null) {
			put("itemsSelector", JsUtils.quotes(itemsSelector));
		}
		if (scanSelector != null) {
			put("scanSelector", JsUtils.quotes(scanSelector));
		}
		if (hiddenElementClass != null) {
			put("hiddenElementClass", JsUtils.quotes(hiddenElementClass));
		}
		if (emptyListClass != null) {
			put("emptyListClass", JsUtils.quotes(emptyListClass));
		}
		if (enabled != null) {
			put("enabled", enabled);
		}
		if (disabledClass != null) {
			put("disabledClass", JsUtils.quotes(disabledClass));
		}
		
		return super.getJavaScriptOptions();
	}

	public String getContainerClass() {
		return containerClass;
	}

	public void setContainerClass(String containerClass) {
		this.containerClass = containerClass;
	}

	public String getInputSelector() {
		return inputSelector;
	}

	public void setInputSelector(String inputSelector) {
		this.inputSelector = inputSelector;
	}

	public String getItemsSelector() {
		return itemsSelector;
	}

	public void setItemsSelector(String itemsSelector) {
		this.itemsSelector = itemsSelector;
	}

	public String getScanSelector() {
		return scanSelector;
	}

	public void setScanSelector(String scanSelector) {
		this.scanSelector = scanSelector;
	}

	public String getHiddenElementClass() {
		return hiddenElementClass;
	}

	public void setHiddenElementClass(String hiddenElementClass) {
		this.hiddenElementClass = hiddenElementClass;
	}

	public String getEmptyListClass() {
		return emptyListClass;
	}

	public void setEmptyListClass(String emptyListClass) {
		this.emptyListClass = emptyListClass;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getDisabledClass() {
		return disabledClass;
	}

	public void setDisabledClass(String disabledClass) {
		this.disabledClass = disabledClass;
	}

}
