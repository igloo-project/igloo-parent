package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.statement;

import org.wicketstuff.wiquery.core.events.EventLabel;

public enum BootstrapConfirmEvent implements EventLabel {

	CONFIRM("confirm"),
	CANCEL("cancel");

	private final String eventLabel;

	private BootstrapConfirmEvent(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	@Override
	public String getEventLabel() {
		return eventLabel;
	}

}
