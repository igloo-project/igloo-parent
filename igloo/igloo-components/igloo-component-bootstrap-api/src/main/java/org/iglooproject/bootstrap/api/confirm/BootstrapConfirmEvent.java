package org.iglooproject.bootstrap.api.confirm;

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
