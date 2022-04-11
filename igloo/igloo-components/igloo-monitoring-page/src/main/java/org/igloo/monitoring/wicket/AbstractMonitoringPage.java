package org.igloo.monitoring.wicket;

import java.nio.charset.StandardCharsets;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.igloo.monitoring.perfdata.HealthLookup;
import org.igloo.monitoring.perfdata.HealthStatusRenderer;
import org.igloo.monitoring.perfdata.IHealthService;

public abstract class AbstractMonitoringPage implements IRequestablePage {

	private static final long serialVersionUID = -1840613019641814018L;

	private static final String MIMETYPE_TEXT = "text/plain";
	private static final String HEADER_X_STATUS = "X-Status";

	protected abstract IHealthService getHealthService();

	protected AbstractMonitoringPage() {
	}

	@Override
	public void renderPage() {
		HealthLookup healthLookup = getHealthService().getHealthLookup();
		new HealthStatusRenderer(healthLookup).getLookupString();
		WebResponse response = (WebResponse) RequestCycle.get().getResponse();
		response.addHeader("Content-Type", MIMETYPE_TEXT + "charset=" + StandardCharsets.UTF_8.displayName());
		response.addHeader("Cache-Control", "no-cache");
		switch (healthLookup.getStatus()) {
		case CRITICAL:
			response.addHeader(HEADER_X_STATUS, "2");
			break;
		case OK:
			response.addHeader(HEADER_X_STATUS, "0");
			break;
		case UNKNOWN:
			response.addHeader(HEADER_X_STATUS, "3");
			break;
		case WARNING:
			response.addHeader(HEADER_X_STATUS, "1");
			break;
		default:
			throw new IllegalStateException(String.format("Unknown status %s", healthLookup.getStatus()));
		}
		response.setStatus(200);
	}
}
