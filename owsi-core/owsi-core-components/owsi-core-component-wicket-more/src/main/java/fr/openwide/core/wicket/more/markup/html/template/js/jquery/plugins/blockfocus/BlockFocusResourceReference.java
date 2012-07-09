package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.blockfocus;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class BlockFocusResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 8162030146784498296L;

	private static final BlockFocusResourceReference INSTANCE = new BlockFocusResourceReference();

	public BlockFocusResourceReference() {
		super(BlockFocusResourceReference.class, "jquery.blockFocus.js");
	}

	public static BlockFocusResourceReference get() {
		return INSTANCE;
	}

}
