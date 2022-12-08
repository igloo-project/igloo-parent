package igloo.bootstrap.tab;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;

import igloo.bootstrap.BootstrapRequestCycle;

public class BootstrapTabBehavior extends Behavior {

	private static final long serialVersionUID = 1645525017124363380L;

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		BootstrapRequestCycle.getSettings().tabRenderHead(component, response);
	}

}