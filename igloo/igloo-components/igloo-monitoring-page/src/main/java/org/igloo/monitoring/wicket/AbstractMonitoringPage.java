package org.igloo.monitoring.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.basic.Label;
import org.igloo.monitoring.HealthLookup;
import org.igloo.monitoring.IHealthService;

public abstract class AbstractMonitoringPage extends Page {
	public static final String TEXT_MIME = "text/plain";
	public static final MarkupType TEXT_PLAIN_MARKUP_TYPE = new MarkupType("txt", TEXT_MIME);

	protected abstract IHealthService getHealthService();

	protected AbstractMonitoringPage() {
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		HealthLookup healthLookup = getHealthService().getHealthLookup();
		add(new Label("status", healthLookup.getStatus()).setEscapeModelStrings(false));
	}
}
