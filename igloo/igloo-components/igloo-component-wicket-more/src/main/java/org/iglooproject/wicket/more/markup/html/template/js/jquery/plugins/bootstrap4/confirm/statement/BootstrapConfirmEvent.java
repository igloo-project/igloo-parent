package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.confirm.statement;

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
