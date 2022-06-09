package igloo.bootstrap4.markup.html.template.js.bootstrap.scrollspy;

import org.wicketstuff.wiquery.core.events.EventLabel;

/**
 * @deprecated see {@link IBootstrapScrollSpy}
 */
@Deprecated(since = "4.3.0")
public enum BootstrapScrollspyEvent implements EventLabel {

	ACTIVATE("activate.bs.scrollspy");

	private final String eventLabel;

	private BootstrapScrollspyEvent(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	@Override
	public String getEventLabel() {
		return eventLabel;
	}

}
