package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.statement;

import org.wicketstuff.wiquery.core.events.EventLabel;

public enum BootstrapModalEvent implements EventLabel {

	SHOW("show"),
	HIDE("hide");

	private final String eventLabel;

	private BootstrapModalEvent(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	@Override
	public String getEventLabel() {
		return eventLabel;
	}
}
