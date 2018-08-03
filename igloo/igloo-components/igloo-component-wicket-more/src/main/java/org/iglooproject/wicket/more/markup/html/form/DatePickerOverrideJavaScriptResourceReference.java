package org.iglooproject.wicket.more.markup.html.form;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

import com.google.common.collect.Lists;

/**
 * Override jQuery UI Datepicker JS: override "Today" button behavior to select the current date and close the datepicker on click.
 */
public final class DatePickerOverrideJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = -1451228678288396852L;
	
	private static final DatePickerOverrideJavaScriptResourceReference INSTANCE = new DatePickerOverrideJavaScriptResourceReference();

	private DatePickerOverrideJavaScriptResourceReference() {
		super(DatePickerOverrideJavaScriptResourceReference.class, "jquery.datePickerOverride.js");
	}
	
	@Override
	protected List<HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static DatePickerOverrideJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
