package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement;

import org.odlabs.wiquery.core.events.EventLabel;

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
