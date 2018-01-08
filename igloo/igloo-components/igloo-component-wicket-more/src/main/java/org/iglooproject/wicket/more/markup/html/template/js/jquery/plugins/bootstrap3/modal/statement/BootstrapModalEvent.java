package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.modal.statement;

import org.wicketstuff.wiquery.core.events.EventLabel;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
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
