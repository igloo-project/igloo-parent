package org.iglooproject.basicapp.web.application.console.common.component;

import org.iglooproject.basicapp.web.application.common.component.EnvironmentPanel;

public class ConsoleEnvironmentPanel extends EnvironmentPanel {

	private static final long serialVersionUID = 6423741720175210102L;

	public ConsoleEnvironmentPanel(String id) {
		super(id);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		setVisible(true);
	}

}