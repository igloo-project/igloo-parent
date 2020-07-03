package org.iglooproject.wicket.bootstrap3.console.maintenance.task.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.component.TaskExecutionResultPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.component.TaskResultPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.component.TaskStatusPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.basic.EnumCoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ConsoleMaintenanceTaskDescriptionPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 7622945973237519021L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceTaskDescriptionPage.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL)
			.enable(SerializationFeature.INDENT_OUTPUT);

	public static final ILinkDescriptorMapper<IPageLinkDescriptor, IModel<QueuedTaskHolder>> MAPPER =
		LinkDescriptorBuilder.start()
				.model(QueuedTaskHolder.class)
				.map(CommonParameters.ID).mandatory()
				.page(ConsoleMaintenanceTaskDescriptionPage.class);
	
	@SpringBean
	private IQueuedTaskHolderManager queuedTaskHolderManager;
	
	@SpringBean
	private IQueuedTaskHolderService queuedTaskHolderService;

	public ConsoleMaintenanceTaskDescriptionPage(PageParameters parameters) {
		super(parameters);
		setOutputMarkupId(true);
		
		final IModel<QueuedTaskHolder> queuedTaskHolderModel = new GenericEntityModel<>(null);
		MAPPER.map(queuedTaskHolderModel).extractSafely(parameters, ConsoleMaintenanceTaskListPage.linkDescriptor(),
				getString("common.notExists"));
		
		addHeadPageTitleKey("console.maintenance.tasks");
		
		WebMarkupContainer statusContainer = new WebMarkupContainer("statusContainer") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				QueuedTaskHolder queuedTaskHolder = queuedTaskHolderModel.getObject();
				String classAttribute = null;
				switch (queuedTaskHolder.getStatus()) {
				case COMPLETED:
					TaskResult result = queuedTaskHolder.getResult();
					if (result == null) {
						classAttribute = "alert-info";
						break;
					}
					switch (result) {
					case SUCCESS:
						classAttribute = "alert-success";
						break;
					case WARN:
						classAttribute = "alert-warning";
						break;
					case ERROR:
					case FATAL:
						classAttribute = "alert-danger";
						break;
					}
					break;
				case TO_RUN:
					classAttribute = "alert-info";
					break;
				case RUNNING:
					classAttribute = "alert-warning";
					break;
				case FAILED:
				case CANCELLED:
				case INTERRUPTED:
				default:
					classAttribute = "alert-danger";
					break;
				}
				tag.append("class", classAttribute, " ");
			}
		};
		add(statusContainer);
		
		statusContainer.add(new EnumCoreLabel<TaskStatus>("status",
				BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().status())) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String resourceKey(TaskStatus value) {
				return "console.maintenance.task.description.mainInformation." + super.resourceKey(value);
			}
		});
		
		statusContainer.add(
				AjaxConfirmLink.<QueuedTaskHolder>build()
						.title(new ResourceModel("console.maintenance.task.description.mainInformation.reload.title"))
						.content(new ResourceModel("console.maintenance.task.description.mainInformation.reload.confirmation"))
						.keepMarkup()
						.yesNo()
						.onClick(new IAjaxAction() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target) {
								try {
									queuedTaskHolderManager.reload(queuedTaskHolderModel.getObject().getId());
									Session.get().success(getString("console.maintenance.task.description.mainInformation.reload.success"));
									throw MAPPER.map(queuedTaskHolderModel).newRestartResponseException();
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
										return queuedTaskHolderService.isReloadable(queuedTaskHolderModel.getObject());
									}
								}.thenShowInternal()
						)
		);
		
		statusContainer.add(
				AjaxConfirmLink.<QueuedTaskHolder>build()
						.title(new ResourceModel("console.maintenance.task.description.mainInformation.cancel.title"))
						.content(new ResourceModel("console.maintenance.task.description.mainInformation.cancel.confirmation"))
						.keepMarkup()
						.yesNo()
						.onClick(new IAjaxAction() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target) {
								try {
									queuedTaskHolderManager.cancel(queuedTaskHolderModel.getObject().getId());
									Session.get().success(getString("console.maintenance.task.description.mainInformation.cancel.success"));
									throw MAPPER.map(queuedTaskHolderModel).newRestartResponseException();
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
										return queuedTaskHolderService.isCancellable(queuedTaskHolderModel.getObject());
									}
								}
										.thenShowInternal()
						)
		);
		
		// Main information detail
		Component queue = new CoreLabel("queue", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().queueId())).hideIfEmpty();
		add(
				new Label("name", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().name())),
				queue,
				new PlaceholderContainer("defaultQueue").condition(Condition.componentVisible(queue)),
				new DateLabel("creationDate", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().creationDate()),
						DatePattern.SHORT_DATETIME),
				new DateLabel("startDate", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().startDate()),
						DatePattern.SHORT_DATETIME),
				new DateLabel("endDate", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().endDate()),
						DatePattern.SHORT_DATETIME),
				new EnumCoreLabel<TaskStatus>("status", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().status())),
				new TaskStatusPanel("statusIcon", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().status())),
				new EnumCoreLabel<TaskResult>("result", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().result())),
				new TaskResultPanel("resultIcon", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().result()))
		);

		// Stack trace
		add(new CoreLabel("stackTrace", BindingModel.of(queuedTaskHolderModel, CoreJpaMoreBindings.queuedTaskHolder().stackTrace()))
				.hideIfEmpty());

		// Serialized task
		add(new Label("serializedTask", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String load() {
				try {
					AbstractTask runnableTask = OBJECT_MAPPER.readValue(queuedTaskHolderModel.getObject().getSerializedTask(), AbstractTask.class);
					return OBJECT_MAPPER.writeValueAsString(runnableTask);
				} catch (Exception e) {
					LOGGER.error("Error parsing the task: " + queuedTaskHolderModel.getObject());
					return "Error parsing the task";
				}
			}
		}));
		
		// Execution result
		add(new TaskExecutionResultPanel("executionResult", queuedTaskHolderModel));
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceTaskListPage.class;
	}
}
