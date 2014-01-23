package fr.openwide.core.wicket.more.markup.html.form;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.odlabs.wiquery.ui.datepicker.DatePickerJavaScriptResourceReference;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class DatePickerOverrideJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = -1451228678288396852L;
	
	private static final DatePickerOverrideJavaScriptResourceReference INSTANCE = new DatePickerOverrideJavaScriptResourceReference();

	private DatePickerOverrideJavaScriptResourceReference() {
		super(DatePickerOverrideJavaScriptResourceReference.class, "jquery.datePickerOverride.js");
	}
	
	@Override
	protected Iterable<? extends HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(DatePickerJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static DatePickerOverrideJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
