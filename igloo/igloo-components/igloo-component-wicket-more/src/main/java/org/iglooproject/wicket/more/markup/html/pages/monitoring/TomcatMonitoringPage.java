package org.iglooproject.wicket.more.markup.html.pages.monitoring;

import java.util.ArrayList;

public class TomcatMonitoringPage extends AbstractMonitoringPage {

  private static final long serialVersionUID = 3818280642304876178L;

  public TomcatMonitoringPage() {
    super();

    setSuccess(true);
    setDetails(new ArrayList<String>(0));
    setMessage("");
  }
}
