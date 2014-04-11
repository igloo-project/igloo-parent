package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.scrollspy;

import org.odlabs.wiquery.core.events.EventLabel;

public enum BootstrapScrollSpyEvent implements EventLabel {

	ACTIVATE("activate.bs.scrollspy");

	private final String eventLabel;

	private BootstrapScrollSpyEvent(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	@Override
	public String getEventLabel() {
		return eventLabel;
	}

}
