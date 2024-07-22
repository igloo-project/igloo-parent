package igloo.console.maintenance.task.component;

import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.console.maintenance.task.page.ConsoleMaintenanceTaskListPage;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.CountLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceTaskTaskManagerPanel extends Panel {

  private static final long serialVersionUID = 6507651164801791278L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConsoleMaintenanceTaskTaskManagerPanel.class);

  @SpringBean private IQueuedTaskHolderManager queuedTaskHolderManager;

  public ConsoleMaintenanceTaskTaskManagerPanel(String id) {
    super(id);

    IModel<Boolean> managerActiveModel =
        new LoadableDetachableModel<Boolean>() {
          private static final long serialVersionUID = 1L;

          @Override
          protected Boolean load() {
            return queuedTaskHolderManager.isActive();
          }
        };

    IModel<Integer> queueSizeModel =
        new LoadableDetachableModel<Integer>() {
          private static final long serialVersionUID = 1L;

          @Override
          protected Integer load() {
            return queuedTaskHolderManager.getNumberOfWaitingTasks();
          }
        };

    Condition managerActiveCondition = Condition.isTrue(managerActiveModel);

    add(
        new TransparentWebMarkupContainer("card")
            .add(
                new ClassAttributeAppender(
                    managerActiveCondition.then("card-success").otherwise("card-danger"))));

    add(
        new CoreLabel(
                "active",
                new StringResourceModel("console.maintenance.task.taskManager.active.${}")
                    .setModel(managerActiveModel))
            .hideIfEmpty());

    add(
        new CountLabel(
            "queueSize", "console.maintenance.task.taskManager.queueSize", queueSizeModel));

    add(
        AjaxConfirmLink.<Void>build()
            .title(new ResourceModel("common.action.confirm.title"))
            .content(new ResourceModel("common.action.confirm.content"))
            .confirm()
            .onClick(
                new IAjaxAction() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void execute(AjaxRequestTarget target) {
                    try {
                      if (queuedTaskHolderManager.isAvailableForAction()) {
                        queuedTaskHolderManager.stop();
                        Session.get().success(getString("common.success"));
                      } else {
                        Session.get()
                            .error(
                                getString(
                                    "console.maintenance.task.taskManager.action.unavailable"));
                      }

                      FeedbackUtils.refreshFeedback(target, getPage());
                      throw new RestartResponseException(ConsoleMaintenanceTaskListPage.class);
                    } catch (RestartResponseException e) {
                      throw e;
                    } catch (Exception e) {
                      LOGGER.error("Unexpected error while trying to stop the task manager.", e);
                      Session.get().error(getString("common.error.unexpected"));
                    }

                    FeedbackUtils.refreshFeedback(target, getPage());
                  }
                })
            .create("stop")
            .add(managerActiveCondition.thenShowInternal()),
        AjaxConfirmLink.<Void>build()
            .title(new ResourceModel("common.action.confirm.title"))
            .content(new ResourceModel("common.action.confirm.content"))
            .confirm()
            .onClick(
                new IAjaxAction() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void execute(AjaxRequestTarget target) {
                    try {
                      if (queuedTaskHolderManager.isAvailableForAction()) {
                        queuedTaskHolderManager.start();
                        Session.get().success(getString("common.success"));
                      } else {
                        Session.get()
                            .error(
                                getString(
                                    "console.maintenance.task.taskManager.action.unavailable"));
                      }

                      FeedbackUtils.refreshFeedback(target, getPage());
                      throw new RestartResponseException(ConsoleMaintenanceTaskListPage.class);
                    } catch (RestartResponseException e) {
                      throw e;
                    } catch (Exception e) {
                      LOGGER.error("Unexpected error while trying to start the task manager.", e);
                      Session.get().error(getString("common.error.unexpected"));
                    }

                    FeedbackUtils.refreshFeedback(target, getPage());
                  }
                })
            .create("start")
            .add(managerActiveCondition.negate().thenShowInternal()));
  }
}
