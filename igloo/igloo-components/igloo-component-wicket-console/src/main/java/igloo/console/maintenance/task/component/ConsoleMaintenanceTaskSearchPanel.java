package igloo.console.maintenance.task.component;

import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSearchQueryData;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class ConsoleMaintenanceTaskSearchPanel extends Panel {

  private static final long serialVersionUID = -3803340118726908397L;

  public ConsoleMaintenanceTaskSearchPanel(
      String id, IPageable pageable, IModel<QueuedTaskHolderSearchQueryData> dataModel) {
    super(id);

    add(
        new PageableSearchForm<>("form", pageable)
            .add(
                new TextField<String>(
                        "name",
                        BindingModel.of(
                            dataModel,
                            CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().name()))
                    .setLabel(new ResourceModel("console.maintenance.task.common.name"))
                    .add(new LabelPlaceholderBehavior()),
                new TaskTypeDropDownMultipleChoice(
                        "taskTypes",
                        BindingModel.of(
                            dataModel,
                            CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().taskTypes()),
                        Suppliers2.arrayList())
                    .setLabel(new ResourceModel("console.maintenance.task.common.taskType"))
                    .add(new LabelPlaceholderBehavior()),
                new TaskStatusDropDownMultipleChoice(
                        "statuses",
                        BindingModel.of(
                            dataModel,
                            CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().statuses()),
                        Suppliers2.arrayList())
                    .setLabel(new ResourceModel("console.maintenance.task.common.status"))
                    .add(new LabelPlaceholderBehavior()),
                new TaskResultDropDownMultipleChoice(
                        "results",
                        BindingModel.of(
                            dataModel,
                            CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().results()),
                        Suppliers2.arrayList())
                    .setLabel(new ResourceModel("console.maintenance.task.common.result"))
                    .add(new LabelPlaceholderBehavior()),
                new TaskQueueIdDropDownMultipleChoice(
                        "queueIds",
                        BindingModel.of(
                            dataModel,
                            CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().queueIds()),
                        Suppliers2.arrayList())
                    .setLabel(new ResourceModel("console.maintenance.task.common.queue"))
                    .add(new LabelPlaceholderBehavior())));
  }
}
