package igloo.wicket.markup.html.panel;

import org.apache.wicket.markup.html.panel.Panel;

public final class InvisiblePanel extends Panel {

	private static final long serialVersionUID = 481176723181209684L;

	public InvisiblePanel(String id) {
		super(id);
		
		setVisible(false);
	}

}
