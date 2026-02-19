package org.igloo.monitoring.service;

public class PerfData {

  private final String name;
  private final String unit;
  private final Number value;
  private final Number warning;
  private final Number critical;
  private final Number min;
  private final Number max;

  public PerfData(
      String name,
      String unit,
      Number value,
      Number warning,
      Number critical,
      Number min,
      Number max) {
    super();
    this.name = name;
    this.unit = unit;
    this.value = value;
    this.warning = warning;
    this.critical = critical;
    this.min = min;
    this.max = max;
  }

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public Number getValue() {
    return value;
  }

  public Number getWarning() {
    return warning;
  }

  public Number getCritical() {
    return critical;
  }

  public Number getMin() {
    return min;
  }

  public Number getMax() {
    return max;
  }
}
