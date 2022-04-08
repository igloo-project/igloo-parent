package org.igloo.monitoring;

public class PerfData {

	private final String name;
	private final String unit;
	private final Number value;
	private final Number warning;
	private final Number critical;
	private final Number min;
	private final Number max;

	public PerfData(String name, String unit, Number value, Number warning, Number critical, Number min, Number max) {
		super();
		this.name = name;
		this.unit = unit;
		this.value = value;
		this.warning = warning;
		this.critical = critical;
		this.min = min;
		this.max = max;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized String getUnit() {
		return unit;
	}

	public synchronized Number getValue() {
		return value;
	}

	public synchronized Number getWarning() {
		return warning;
	}

	public synchronized Number getCritical() {
		return critical;
	}

	public synchronized Number getMin() {
		return min;
	}

	public synchronized Number getMax() {
		return max;
	}
}
