package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fileuploadglue;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fileupload.FileUploadJavaScriptResourceReference;

public final class FileUploadGlueJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = -4906439533938126355L;

	private static final FileUploadGlueJavaScriptResourceReference INSTANCE = new FileUploadGlueJavaScriptResourceReference();

	private FileUploadGlueJavaScriptResourceReference() {
		super(FileUploadGlueJavaScriptResourceReference.class, "jquery.fileuploadglue.js");
	}

	public static FileUploadGlueJavaScriptResourceReference get() {
		return INSTANCE;
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		return JavaScriptHeaderItems.forReferences(FileUploadJavaScriptResourceReference.get());
	}

}
