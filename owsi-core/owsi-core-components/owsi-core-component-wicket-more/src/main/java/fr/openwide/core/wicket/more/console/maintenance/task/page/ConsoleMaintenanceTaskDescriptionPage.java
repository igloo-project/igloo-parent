package fr.openwide.core.wicket.more.console.maintenance.task.page;

import java.util.Date;
import java.util.List;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolderBinding;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.CoreLinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.utils.CoreLinkParameterUtils;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class ConsoleMaintenanceTaskDescriptionPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 7622945973237519021L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceTaskDescriptionPage.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL)
			.enable(SerializationFeature.INDENT_OUTPUT);

	@SpringBean
	private IQueuedTaskHolderService queuedTaskHolderService;

	@SpringBean
	private IQueuedTaskHolderManager queuedTaskHolderManager;

	protected IModel<QueuedTaskHolder> queuedTaskHolderModel;

	private static final List<TaskStatus> RELOADBLE_TASK_STATUS = Lists.newArrayList(TaskStatus.CANCELLED,
			TaskStatus.FAILED, TaskStatus.INTERRUPTED);
	
	public static IPageLinkDescriptor linkDescriptor(IModel<? extends QueuedTaskHolder> queuedTaskHolderModel) {
		QueuedTaskHolderBinding binding = new QueuedTaskHolderBinding();
		return new CoreLinkDescriptorBuilder()
				.page(ConsoleMaintenanceTaskDescriptionPage.class)
				.parameter(CoreLinkParameterUtils.ID_PARAMETER, BindingModel.of(queuedTaskHolderModel, binding.id()))
				.build();
	}

	public ConsoleMaintenanceTaskDescriptionPage(PageParameters parameters) {
		super(parameters);
		setOutputMarkupId(true);

		this.queuedTaskHolderModel = CoreLinkParameterUtils.extractGenericEntityParameterModel(queuedTaskHolderService, parameters, Long.class);

		if (this.queuedTaskHolderModel == null || this.queuedTaskHolderModel.getObject() == null) {
			Session.get().error(getString("common.notExists"));
			throw new RestartResponseException(ConsoleMaintenanceTaskListPage.class);
		}

		addHeadPageTitleKey("console.maintenance.tasks");

		WebMarkupContainer statusContainer = new WebMarkupContainer("statusContainer");
		statusContainer.add(new ClassAttributeAppender(new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				switch (queuedTaskHolderModel.getObject().getStatus()) {
				case COMPLETED:
					return "alert-success";
				case TO_RUN:
					return "alert-info";
				case RUNNING:
					return "alert-warning";
				default:
					return "alert-error";
				}
			}
		}));
		add(statusContainer);

		IModel<String> taskStatusStringModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				switch (queuedTaskHolderModel.getObject().getStatus()) {
				case COMPLETED:
					return "success";
				case TO_RUN:
					return "toRun";
				case RUNNING:
					return "running";
				default:
					return "error";
				}
			}
		};

		statusContainer.add(new Label("status", new StringResourceModel(
				"console.maintenance.task.description.mainInformation.status.${}", taskStatusStringModel,
				queuedTaskHolderModel.getObject().getStatus())));

		statusContainer.add(new AjaxConfirmLink<QueuedTaskHolder>("reload", queuedTaskHolderModel, new ResourceModel(
				"console.maintenance.task.description.mainInformation.reload.title"), new ResourceModel(
				"console.maintenance.task.description.mainInformation.reload.confirmation"), new ResourceModel(
				"common.yes"), new ResourceModel("common.no"), null, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					queuedTaskHolderManager.reload(getModelObject().getId());
					Session.get().success(
							getString("console.maintenance.task.description.mainInformation.reload.success"));
					throw linkDescriptor(getModel()).restartResponseException();
				} catch (RestartResponseException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.error("Unexpected error while reloading task", e);
					getSession().error(getString("common.error.unexpected"));
				}

				FeedbackUtils.refreshFeedback(target, getPage());
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(RELOADBLE_TASK_STATUS.contains(getModelObject().getStatus()));
			}
		});

		add(new Label("name", new PropertyModel<String>(queuedTaskHolderModel.getObject(), "name")));
		add(new DateLabel("creationDate", new PropertyModel<Date>(queuedTaskHolderModel.getObject(), "creationDate"),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("startDate", new PropertyModel<Date>(queuedTaskHolderModel.getObject(), "startDate"),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("endDate", new PropertyModel<Date>(queuedTaskHolderModel.getObject(), "endDate"),
				DatePattern.SHORT_DATETIME));

		add(new HideableLabel("result", new PropertyModel<String>(queuedTaskHolderModel.getObject(), "result")));

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
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceTaskListPage.class;
	}
}
