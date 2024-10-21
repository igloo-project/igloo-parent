package igloo.console.maintenance.template;

import igloo.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import igloo.console.template.ConsoleTemplate;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ConsoleMaintenanceTemplate extends ConsoleTemplate {

  private static final long serialVersionUID = -3192604063259001201L;

  protected ConsoleMaintenanceTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("console.navigation.maintenance")));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ConsoleMaintenanceSearchPage.class;
  }
}
