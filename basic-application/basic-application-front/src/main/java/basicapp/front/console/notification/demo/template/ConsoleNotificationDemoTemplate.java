package basicapp.front.console.notification.demo.template;

import basicapp.front.console.notification.demo.page.ConsoleNotificationDemoListPage;
import igloo.console.template.ConsoleTemplate;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ConsoleNotificationDemoTemplate extends ConsoleTemplate {

  private static final long serialVersionUID = 1L;

  protected ConsoleNotificationDemoTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("console.navigation.notification"),
            ConsoleNotificationDemoListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ConsoleNotificationDemoListPage.class;
  }
}
