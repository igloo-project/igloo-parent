package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fileupload;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;
import org.odlabs.wiquery.ui.core.CoreUIJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.json.JsonJavascriptResourceReference;

public final class FileUploadJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = -670753780950526435L;

	private static final FileUploadJavaScriptResourceReference INSTANCE = new FileUploadJavaScriptResourceReference();

	private FileUploadJavaScriptResourceReference() {
		super(FileUploadJavaScriptResourceReference.class, "jquery.fileupload.js");
	}

	public static FileUploadJavaScriptResourceReference get() {
		return INSTANCE;
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		return JavaScriptHeaderItems.forReferences(
				CoreUIJavaScriptResourceReference.get(),
				WidgetJavaScriptResourceReference.get(),
				JsonJavascriptResourceReference.get()
		);
	}

}
