package igloo.console.maintenance.task.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import igloo.bootstrap.common.BootstrapColor;
import igloo.bootstrap.common.IBootstrapColor;
import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap5.markup.html.bootstrap.component.BootstrapBadge;
import igloo.console.maintenance.task.component.ConsoleMaintenanceTaskBatchReportPanel;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.autoconfigure.TaskAutoConfiguration;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.more.console.maintenance.task.model.BatchReportBeanModel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.more.rendering.TaskResultRenderer;
import org.iglooproject.wicket.more.rendering.TaskStatusRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

public class ConsoleMaintenanceTaskDetailPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = 7622945973237519021L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConsoleMaintenanceTaskDetailPage.class);

  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL)
          .enable(SerializationFeature.INDENT_OUTPUT);

  public static final ILinkDescriptorMapper<IPageLinkDescriptor, IModel<QueuedTaskHolder>> MAPPER =
      LinkDescriptorBuilder.start()
          .model(QueuedTaskHolder.class)
          .map(CommonParameters.ID)
          .mandatory()
          .page(ConsoleMaintenanceTaskDetailPage.class);

  @SpringBean private IQueuedTaskHolderManager queuedTaskHolderManager;

  @SpringBean private IQueuedTaskHolderService queuedTaskHolderService;

  @SpringBean(name = TaskAutoConfiguration.OBJECT_MAPPER_BEAN_NAME)
  @Qualifier()
  private ObjectMapper queuedTaskHolderObjectMapper;

  public ConsoleMaintenanceTaskDetailPage(PageParameters parameters) {
    super(parameters);
    setOutputMarkupId(true);

    IModel<QueuedTaskHolder> queuedTaskHolderModel = new GenericEntityModel<>();

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("console.maintenance.tasks"),
            ConsoleMaintenanceTaskListPage.linkDescriptor()));
    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("console.maintenance.task.common.task")));

    MAPPER
        .map(queuedTaskHolderModel)
        .extractSafely(
            parameters,
            ConsoleMaintenanceTaskListPage.linkDescriptor(),
            getString("common.error.unexpected"));

    IModel<IBootstrapColor> statusColorModel =
        new LoadableDetachableModel<IBootstrapColor>() {
          private static final long serialVersionUID = 1L;

          @Override
          protected IBootstrapColor load() {
            QueuedTaskHolder queuedTaskHolder = queuedTaskHolderModel.getObject();

            if (TaskStatus.COMPLETED.equals(queuedTaskHolder.getStatus())) {
              TaskResult result = queuedTaskHolder.getResult();

              if (result == null) {
                return BootstrapColor.INFO;
              }

              switch (result) {
                case SUCCESS:
                  return BootstrapColor.SUCCESS;
                case WARN:
                  return BootstrapColor.WARNING;
                case ERROR:
                case FATAL:
                  return BootstrapColor.DANGER;
              }
            }

            return TaskStatusRenderer.get().renderColor(queuedTaskHolder.getStatus(), getLocale());
          }
        };

    add(
        new WebMarkupContainer("statusContainer")
            .add(
                new CoreLabel(
                        "status",
                        EnumRenderer.withPrefix("console.maintenance.task.detail.mainInformation")
                            .asModel(
                                BindingModel.of(
                                    queuedTaskHolderModel,
                                    CoreJpaMoreBindings.queuedTaskHolder().status())))
                    .hideIfEmpty(),
                new EnclosureContainer("actionsContainer")
                    .anyChildVisible()
                    .add(
                        AjaxConfirmLink.<QueuedTaskHolder>build()
                            .title(
                                new ResourceModel(
                                    "console.maintenance.task.detail.mainInformation.reload.title"))
                            .content(
                                new ResourceModel(
                                    "console.maintenance.task.detail.mainInformation.reload.confirmation"))
                            .keepMarkup()
                            .yesNo()
                            .onClick(
                                new IAjaxAction() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void execute(AjaxRequestTarget target) {
                                    try {
                                      queuedTaskHolderManager.reload(
                                          queuedTaskHolderModel.getObject().getId());
                                      Session.get()
                                          .success(
                                              getString(
                                                  "console.maintenance.task.detail.mainInformation.reload.success"));
                                      throw MAPPER
                                          .map(queuedTaskHolderModel)
                                          .newRestartResponseException();
                                    } catch (RestartResponseException e) {
                                      throw e;
                                    } catch (Exception e) {
                                      LOGGER.error("Unexpected error while reloading task", e);
                                      Session.get().error(getString("common.error.unexpected"));
                                    }
                                    FeedbackUtils.refreshFeedback(target, getPage());
                                  }
                                })
                            .create("reload", queuedTaskHolderModel)
                            .add(
                                new Condition() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public boolean applies() {
                                    return queuedTaskHolderService.isReloadable(
                                        queuedTaskHolderModel.getObject());
                                  }
                                }.thenShowInternal()),
                        AjaxConfirmLink.<QueuedTaskHolder>build()
                            .title(
                                new ResourceModel(
                                    "console.maintenance.task.detail.mainInformation.cancel.title"))
                            .content(
                                new ResourceModel(
                                    "console.maintenance.task.detail.mainInformation.cancel.confirmation"))
                            .keepMarkup()
                            .yesNo()
                            .onClick(
                                new IAjaxAction() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void execute(AjaxRequestTarget target) {
                                    try {
                                      queuedTaskHolderManager.cancel(
                                          queuedTaskHolderModel.getObject().getId());
                                      Session.get()
                                          .success(
                                              getString(
                                                  "console.maintenance.task.detail.mainInformation.cancel.success"));
                                      throw MAPPER
                                          .map(queuedTaskHolderModel)
                                          .newRestartResponseException();
                                    } catch (RestartResponseException e) {
                                      throw e;
                                    } catch (Exception e) {
                                      LOGGER.error("Unexpected error while cancelling task", e);
                                      Session.get().error(getString("common.error.unexpected"));
                                    }
                                    FeedbackUtils.refreshFeedback(target, getPage());
                                  }
                                })
                            .create("cancel", queuedTaskHolderModel)
                            .add(
                                new Condition() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public boolean applies() {
                                    return queuedTaskHolderService.isCancellable(
                                        queuedTaskHolderModel.getObject());
                                  }
                                }.thenShowInternal())))
            .add(BootstrapColorBehavior.alert(statusColorModel)));

    Component queue =
        new CoreLabel(
                "queue",
                BindingModel.of(
                    queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().queueId()))
            .hideIfEmpty();
    Component status =
        new BootstrapBadge<>(
            "status",
            BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().status()),
            TaskStatusRenderer.get());
    Component result =
        new BootstrapBadge<>(
            "result",
            BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().result()),
            TaskResultRenderer.get());

    add(
        new CoreLabel(
                "name",
                BindingModel.of(
                    queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().name()))
            .showPlaceholder(),
        queue,
        new PlaceholderContainer("defaultQueue").condition(Condition.componentVisible(queue)),
        new CoreLabel(
                "creationDate",
                BindingModel.of(
                    queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().creationDate()))
            .showPlaceholder(),
        new CoreLabel(
                "startDate",
                BindingModel.of(
                    queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().startDate()))
            .showPlaceholder(),
        new CoreLabel(
                "endDate",
                BindingModel.of(
                    queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().endDate()))
            .showPlaceholder(),
        status,
        new DefaultPlaceholderPanel("statusPlaceholder")
            .condition(Condition.componentVisible(status)),
        result,
        new DefaultPlaceholderPanel("resultPlaceholder")
            .condition(Condition.componentVisible(result)));

    add(
        new CoreLabel(
            "serializedTask",
            new LoadableDetachableModel<String>() {
              private static final long serialVersionUID = 1L;

              @Override
              protected String load() {
                try {
                  AbstractTask runnableTask =
                      OBJECT_MAPPER.readValue(
                          queuedTaskHolderModel.getObject().getSerializedTask(),
                          AbstractTask.class);
                  return OBJECT_MAPPER.writeValueAsString(runnableTask);
                } catch (Exception e) {
                  LOGGER.error("Error parsing the task: " + queuedTaskHolderModel.getObject(), e);
                  return "Error parsing the task";
                }
              }
            }));

    add(
        new CoreLabel(
                "stackTrace",
                BindingModel.of(
                    queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().stackTrace()))
            .hideIfEmpty());

    add(
        new ConsoleMaintenanceTaskBatchReportPanel(
            "batchReport", BatchReportBeanModel.fromTask(queuedTaskHolderModel)));
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenanceTaskDetailPage.class;
  }
}
