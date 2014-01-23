package fr.openwide.core.wicket.more.console.maintenance.task.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBinding;

public class ConsoleMaintenanceTaskDescriptionPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 7622945973237519021L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceTaskDescriptionPage.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL)
			.enable(SerializationFeature.INDENT_OUTPUT);

	@SpringBean
	private IQueuedTaskHolderManager queuedTaskHolderManager;
	
	@SpringBean
	private IQueuedTaskHolderService queuedTaskHolderService;

	public static IPageLinkDescriptor linkDescriptor(IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		return new LinkDescriptorBuilder()
				.page(ConsoleMaintenanceTaskDescriptionPage.class)
				.map(CommonParameters.ID, queuedTaskHolderModel, QueuedTaskHolder.class).mandatory()
				.build();
	}

	public ConsoleMaintenanceTaskDescriptionPage(PageParameters parameters) {
		super(parameters);
		setOutputMarkupId(true);
		
		final IModel<QueuedTaskHolder> queuedTaskHolderModel = new GenericEntityModel<Long, QueuedTaskHolder>(null);
		linkDescriptor(queuedTaskHolderModel).extractSafely(parameters, ConsoleMaintenanceTaskListPage.linkDescriptor(),
				getString("common.notExists"));
		
		addHeadPageTitleKey("console.maintenance.tasks");
		
		WebMarkupContainer statusContainer = new WebMarkupContainer("statusContainer");
		statusContainer.add(new ClassAttributeAppender(new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String load() {
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

		IModel<String> taskStatusStringModel = new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String load() {
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
				BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().status()))));

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
					throw linkDescriptor(getModel()).newRestartResponseException();
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
				setVisible(queuedTaskHolderService.isReloadable(getModelObject()));
			}
		});

		statusContainer.add(new AjaxConfirmLink<QueuedTaskHolder>("cancel", queuedTaskHolderModel, new ResourceModel(
				"console.maintenance.task.description.mainInformation.cancel.title"), new ResourceModel(
				"console.maintenance.task.description.mainInformation.cancel.confirmation"), new ResourceModel(
				"common.yes"), new ResourceModel("common.no"), null, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					queuedTaskHolderManager.cancel(getModelObject().getId());
					Session.get().success(
							getString("console.maintenance.task.description.mainInformation.cancel.success"));
					throw linkDescriptor(getModel()).newRestartResponseException();
				} catch (RestartResponseException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.error("Unexpected error while cancelling task", e);
					getSession().error(getString("common.error.unexpected"));
				}
				
				FeedbackUtils.refreshFeedback(target, getPage());
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(queuedTaskHolderService.isCancellable(getModelObject()));
			}
		});
		
		add(new Label("name", BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().name())));
		Component queue = new HideableLabel("queue", BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().queueId()));
		add(queue, new PlaceholderContainer("defaultQueue").component(queue));
		add(new DateLabel("creationDate", BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().creationDate()),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("startDate", BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().startDate()),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("endDate", BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().endDate()),
				DatePattern.SHORT_DATETIME));

		add(new HideableLabel("result", BindingModel.of(queuedTaskHolderModel, CoreWicketMoreBinding.queuedTaskHolderBinding().result())));

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
