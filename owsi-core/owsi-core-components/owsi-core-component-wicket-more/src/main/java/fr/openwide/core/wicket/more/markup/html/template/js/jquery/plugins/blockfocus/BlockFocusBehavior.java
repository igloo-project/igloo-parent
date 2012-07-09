package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.blockfocus;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BlockFocusBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = -1285953685103587762L;

	private IBlockFocus blockFocus;

	public BlockFocusBehavior(IBlockFocus blockFocus) {
		super();
		
		this.blockFocus = blockFocus;
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(blockFocus);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.renderJavaScriptReference(BlockFocusResourceReference.get());
	}

}
