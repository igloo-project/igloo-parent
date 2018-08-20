package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.fileuploadglue;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.fileupload.FileUploadJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class FileUploadGlueJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -4906439533938126355L;

	private static final FileUploadGlueJavaScriptResourceReference INSTANCE = new FileUploadGlueJavaScriptResourceReference();

	private FileUploadGlueJavaScriptResourceReference() {
		super(FileUploadGlueJavaScriptResourceReference.class, "jquery.fileuploadglue.js");
	}

	public static FileUploadGlueJavaScriptResourceReference get() {
		return INSTANCE;
	}

	@Override
	public List<HeaderItem> getPluginDependencies() {
		return forReferences(FileUploadJavaScriptResourceReference.get());
	}

}
