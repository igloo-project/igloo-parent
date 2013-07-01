package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.datepicker;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;
import org.odlabs.wiquery.ui.core.CoreUIJavaScriptResourceReference;

public final class DatePickerResourceReference extends JavaScriptResourceReference {
	
	private static final long serialVersionUID = 3479345888453278033L;
	
	private static final DatePickerResourceReference INSTANCE = new DatePickerResourceReference();
	
	public DatePickerResourceReference() {
		super(DatePickerResourceReference.class, "jquery.ui.datepicker.js");
	}
	
	public static DatePickerResourceReference get() {
		return INSTANCE;
	}

	@Override
	public Iterable< ? extends HeaderItem> getDependencies()
	{
		return JavaScriptHeaderItems.forReferences(CoreUIJavaScriptResourceReference.get());
	}
}
