package org.igloo.monitoring.perfdata;

import java.util.List;

public class HealthLookup {

	private final String name;
	private final HealthStatus status;
	private final String message;
	private final List<PerfData> perfDatas;

	public HealthLookup(String name, HealthStatus status, String message, List<PerfData> perfDatas) {
		super();
		this.name = name;
		this.status = status;
		this.message = message;
		this.perfDatas = perfDatas;
	}

	public synchronized String getName() {
		return name;
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
