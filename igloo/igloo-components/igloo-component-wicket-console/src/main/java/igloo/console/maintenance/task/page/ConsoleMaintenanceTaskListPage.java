package igloo.console.maintenance.task.page;

import igloo.console.maintenance.task.component.ConsoleMaintenanceTaskSearchPanel;
import igloo.console.maintenance.task.component.ConsoleMaintenanceTaskTaskManagerPanel;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.util.DatePattern;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.more.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.rendering.TaskResultRenderer;
import org.iglooproject.wicket.more.rendering.TaskStatusRenderer;

public class ConsoleMaintenanceTaskListPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = -4085517848301335018L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ConsoleMaintenanceTaskListPage.class);
  }

  public ConsoleMaintenanceTaskListPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.tasks")));

    add(new ConsoleMaintenanceTaskTaskManagerPanel("taskManager"));

    QueuedTaskHolderDataProvider dataProvider = new QueuedTaskHolderDataProvider();

    DecoratedCoreDataTablePanel<QueuedTaskHolder, ?> results =
        DataTableBuilder.start(dataProvider)
            .addBootstrapBadgeColumn(
                new ResourceModel("console.maintenance.task.common.status"),
                CoreJpaMoreBindings.queuedTaskHolder().status(),
                TaskStatusRenderer.get())
            .withClass("cell-w-200")
            .addBootstrapBadgeColumn(
                new ResourceModel("console.maintenance.task.common.result"),
                CoreJpaMoreBindings.queuedTaskHolder().result(),
                TaskResultRenderer.get())
            .withClass("cell-w-150")
            .addLabelColumn(
                new ResourceModel("console.maintenance.task.common.name"),
                CoreJpaMoreBindings.queuedTaskHolder().name())
            .withLink(ConsoleMaintenanceTaskDetailPage.MAPPER)
            .withClass("cell-w-200")
            .addLabelColumn(
                new ResourceModel("console.maintenance.task.common.queue"),
                CoreJpaMoreBindings.queuedTaskHolder().queueId())
            .showPlaceholder(new ResourceModel("console.maintenance.task.common.queue.default"))
            .withClass("cell-w-200")
            .addLabelColumn(
                new ResourceModel("console.maintenance.task.common.creationDate"),
                CoreJpaMoreBindings.queuedTaskHolder().creationDate(),
                DatePattern.SHORT_DATETIME)
            .withClass("cell-w-150")
            .addLabelColumn(
                new ResourceModel("console.maintenance.task.common.startDate"),
                CoreJpaMoreBindings.queuedTaskHolder().startDate(),
                DatePattern.SHORT_DATETIME)
            .withClass("cell-w-150")
            .addLabelColumn(
                new ResourceModel("console.maintenance.task.common.endDate"),
                CoreJpaMoreBindings.queuedTaskHolder().endDate(),
                DatePattern.SHORT_DATETIME)
            .withClass("cell-w-150")
            .bootstrapCard()
            .count("console.maintenance.task.common.count")
            .ajaxPagers()
            .build("results", 30);

    add(new ConsoleMaintenanceTaskSearchPanel("search", results, dataProvider), results);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenanceTaskListPage.class;
  }
}
