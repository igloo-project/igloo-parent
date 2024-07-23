package org.iglooproject.wicket.more.markup.html.pages.monitoring;

import java.util.Collections;
import java.util.List;

public class DummyMonitoringPage extends AbstractMonitoringPage {

  private static final long serialVersionUID = -8058000817486549210L;

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
