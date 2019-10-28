package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.monthpicker;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

import com.google.common.collect.ImmutableList;

public final class MonthPickerJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1451228678288396852L;

	private static final MonthPickerJavaScriptResourceReference INSTANCE = new MonthPickerJavaScriptResourceReference();

	private MonthPickerJavaScriptResourceReference() {
		super(MonthPickerJavaScriptResourceReference.class, "jquery.ui.monthpicker.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return ImmutableList.of(
			JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get())
		);
	}

	public static MonthPickerJavaScriptResourceReference get() {
		return INSTANCE;
	}

}

