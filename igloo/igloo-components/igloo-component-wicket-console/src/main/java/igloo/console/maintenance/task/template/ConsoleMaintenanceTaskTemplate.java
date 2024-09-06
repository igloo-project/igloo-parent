package igloo.console.maintenance.task.template;

import igloo.console.maintenance.task.page.ConsoleMaintenanceTaskListPage;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ConsoleMaintenanceTaskTemplate extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = 1L;

  protected ConsoleMaintenanceTaskTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("console.navigation.maintenance.task"),
            ConsoleMaintenanceTaskListPage.linkDescriptor()));
  }
}
