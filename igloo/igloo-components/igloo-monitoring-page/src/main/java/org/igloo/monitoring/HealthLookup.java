package org.igloo.monitoring;

import java.util.List;

public class HealthLookup {

	private final HealthStatus status;
	private final String message;
	private final List<PerfData> perfDatas;

	public HealthLookup(HealthStatus status, String message, List<PerfData> perfDatas) {
		super();
		this.status = status;
		this.message = message;
		this.perfDatas = perfDatas;
	}

	public synchronized HealthStatus getStatus() {
		return status;
	}

	public synchronized String getMessage() {
		return message;
	}

	public synchronized List<PerfData> getPerfDatas() {
		return perfDatas;
	}

}
