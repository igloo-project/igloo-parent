package fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.collections.MiniMap;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ZeroClipboardBehavior extends Behavior {

	private static final long serialVersionUID = 2935399893580511345L;

	private static final String ZERO_CLIPBOARD_LINK_JS = "ZeroClipboardLink.js";
	
	private static final String ZERO_CLIPBOARD_FLASH_URL = "zeroClipboardFlashUrl";
	
	public static IResourceLinkDescriptor linkZeroClipboardFlash() {
		return new LinkDescriptorBuilder()
						.resource(ZeroClipboardFlashResourceReference.get())
						.build();
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptReferenceHeaderItem.forReference(ZeroClipboardJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(getTemplate().asString(getVariables())));
	}

	private static TextTemplate getTemplate() {
		return new PackageTextTemplate(ZeroClipboardBehavior.class, ZERO_CLIPBOARD_LINK_JS);
	}

	private static Map<String, Object> getVariables() {
		Map<String, Object> variables = new MiniMap<String, Object>(1);
		variables.put(ZERO_CLIPBOARD_FLASH_URL, ZeroClipboardBehavior.linkZeroClipboardFlash().fullUrl());
		return variables;
	}
}
