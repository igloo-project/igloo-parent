package org.igloo.monitoring.service;

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

  public String getName() {
    return name;
  }

  public HealthStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public List<PerfData> getPerfDatas() {
    return perfDatas;
  }
}
