package basicapp.front.console.notification.demo.template;

import basicapp.front.console.notification.demo.page.ConsoleNotificationDemoListPage;
import igloo.console.template.ConsoleTemplate;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class ConsoleNotificationDemoTemplate extends ConsoleTemplate {

  private static final long serialVersionUID = 1L;

  protected ConsoleNotificationDemoTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ConsoleNotificationDemoListPage.class;
  }
}
