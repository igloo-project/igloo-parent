package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.scrollspy;

import org.wicketstuff.wiquery.core.events.EventLabel;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
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
