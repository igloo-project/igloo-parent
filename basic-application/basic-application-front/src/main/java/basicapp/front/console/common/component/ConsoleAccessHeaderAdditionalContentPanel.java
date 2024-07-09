package basicapp.front.console.common.component;

import org.apache.wicket.markup.html.panel.Panel;

public class ConsoleAccessHeaderAdditionalContentPanel extends Panel {

	private static final long serialVersionUID = 1434180247335441796L;

	public ConsoleAccessHeaderAdditionalContentPanel(String id) {
		super(id);
		
		add(new ConsoleAccessEnvironmentPanel("environment"));
	}

}
