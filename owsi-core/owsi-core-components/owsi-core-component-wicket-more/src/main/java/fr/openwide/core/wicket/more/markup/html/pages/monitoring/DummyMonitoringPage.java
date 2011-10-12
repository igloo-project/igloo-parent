package fr.openwide.core.wicket.more.markup.html.pages.monitoring;

import java.util.Collections;
import java.util.List;

public class DummyMonitoringPage extends AbstractMonitoringPage {

	public DummyMonitoringPage() {
		super();
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public List<String> getDetails() {
		return Collections.emptyList();
	}

	@Override
	public String getMessage() {
		return "";
	}

}
