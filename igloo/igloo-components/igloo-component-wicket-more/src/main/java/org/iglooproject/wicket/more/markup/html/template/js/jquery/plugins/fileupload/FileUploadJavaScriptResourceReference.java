package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.fileupload;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.json.JsonJavascriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

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
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get()));
		dependencies.add(JavaScriptHeaderItem.forReference(JsonJavascriptResourceReference.get()));
		return dependencies;
	}

}
