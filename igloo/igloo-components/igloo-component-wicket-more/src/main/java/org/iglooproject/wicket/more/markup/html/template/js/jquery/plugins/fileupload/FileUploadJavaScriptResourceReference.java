package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.fileupload;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.wicketstuff.wiquery.ui.core.CoreUIJavaScriptResourceReference;
import org.wicketstuff.wiquery.ui.widget.WidgetJavaScriptResourceReference;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.json.JsonJavascriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class FileUploadJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -670753780950526435L;

	private static final FileUploadJavaScriptResourceReference INSTANCE = new FileUploadJavaScriptResourceReference();

	private FileUploadJavaScriptResourceReference() {
		super(FileUploadJavaScriptResourceReference.class, "jquery.fileupload.js");
	}

	public static FileUploadJavaScriptResourceReference get() {
		return INSTANCE;
	}

	@Override
	public List<HeaderItem> getPluginDependencies() {
		return forReferences(
				CoreUIJavaScriptResourceReference.get(),
				WidgetJavaScriptResourceReference.get(),
				JsonJavascriptResourceReference.get()
		);
	}

}
