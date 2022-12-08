package igloo.bootstrap.confirm;

import org.wicketstuff.wiquery.core.events.EventLabel;

public enum BootstrapConfirmEvent implements EventLabel {

	CONFIRM("confirm.bs.confirm"),
	CANCEL("cancel.bs.confirm");

	private final String eventLabel;

	private BootstrapConfirmEvent(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	@Override
	public String getEventLabel() {
		return eventLabel;
	}

}
