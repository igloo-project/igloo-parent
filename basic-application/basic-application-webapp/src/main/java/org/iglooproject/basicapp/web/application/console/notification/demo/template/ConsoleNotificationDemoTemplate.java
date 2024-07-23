package org.iglooproject.basicapp.web.application.console.notification.demo.template;

import igloo.console.template.ConsoleTemplate;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.console.notification.demo.page.ConsoleNotificationDemoIndexPage;

public abstract class ConsoleNotificationDemoTemplate extends ConsoleTemplate {

  private static final long serialVersionUID = -3192604063259001201L;

  public ConsoleNotificationDemoTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ConsoleNotificationDemoIndexPage.class;
  }
}
