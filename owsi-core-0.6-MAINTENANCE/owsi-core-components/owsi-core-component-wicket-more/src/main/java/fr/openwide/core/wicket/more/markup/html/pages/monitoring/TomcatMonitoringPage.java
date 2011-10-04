package fr.openwide.core.wicket.more.markup.html.pages.monitoring;

import java.util.ArrayList;

public class TomcatMonitoringPage extends AbstractMonitoringPage {

	public TomcatMonitoringPage() {
		super();
		
		setSuccess(true);
		setDetails(new ArrayList<String>(0));
		setMessage("");
	}

}