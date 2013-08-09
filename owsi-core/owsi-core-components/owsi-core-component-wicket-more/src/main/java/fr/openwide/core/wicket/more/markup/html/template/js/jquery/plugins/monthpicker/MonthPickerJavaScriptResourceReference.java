package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.monthpicker;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.odlabs.wiquery.ui.datepicker.DatePickerJavaScriptResourceReference;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class MonthPickerJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = -1451228678288396852L;
	
	private static final MonthPickerJavaScriptResourceReference INSTANCE = new MonthPickerJavaScriptResourceReference();

	private MonthPickerJavaScriptResourceReference() {
		super(MonthPickerJavaScriptResourceReference.class, "jquery.ui.monthpicker.js");
	}
	
	@Override
	protected Iterable<? extends HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(DatePickerJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static MonthPickerJavaScriptResourceReference get() {
		return INSTANCE;
	}
}

