package fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard;

import org.apache.wicket.request.resource.PackageResourceReference;

public final class ZeroClipboardFlashResourceReference extends PackageResourceReference {

	private static final long serialVersionUID = 4799694753576330449L;

	private static final ZeroClipboardFlashResourceReference INSTANCE = new ZeroClipboardFlashResourceReference();

	private ZeroClipboardFlashResourceReference() {
		super(ZeroClipboardFlashResourceReference.class, "ZeroClipboard.swf");
	}

	public static ZeroClipboardFlashResourceReference get() {
		return INSTANCE;
	}

}
