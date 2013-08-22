package fr.openwide.core.wicket.more.console.maintenance.task.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CountLabel;
import fr.openwide.core.wicket.more.console.maintenance.task.page.ConsoleMaintenanceTaskListPage;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;

public class TaskQueueInformationPanel extends Panel {

	private static final long serialVersionUID = 6507651164801791278L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskQueueInformationPanel.class);

	@SpringBean
	private IQueuedTaskHolderManager queuedTaskHolderManager;

	public TaskQueueInformationPanel(String id) {
		super(id);
		WebMarkupContainer statusContainer = new WebMarkupContainer("statusContainer");
		add(statusContainer);
		statusContainer.add(new ClassAttributeAppender(new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (queuedTaskHolderManager.isActive()) {
					return "alert-success";
				} else {
					return "alert-error";
				}
			}
		}));

		IModel<String> queueStatusStringModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (queuedTaskHolderManager.isActive()) {
					return "active";
				} else {
					return "inactive";
				}
			}
		};

		statusContainer.add(new Label("status", new StringResourceModel("console.maintenance.task.queue.status.${}",
				queueStatusStringModel)));

		add(new CountLabel("queueSize", "console.maintenance.task.queue.queueSize", new Model<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				return queuedTaskHolderManager.getNumberOfWaitingTasks();
			}
		}));

		add(new AjaxLink<Void>("stop") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(queuedTaskHolderManager.isActive());
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					if (queuedTaskHolderManager.isAvailableForAction()) {
						queuedTaskHolderManager.stop();
						getSession().success(getString("console.maintenance.task.queue.stop.success"));
					} else {
						getSession().error(getString("console.maintenance.task.queue.action.unavailable"));
					}

					FeedbackUtils.refreshFeedback(target, getPage());
					throw new RestartResponseException(ConsoleMaintenanceTaskListPage.class);
				} catch (RestartResponseException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.error("Unexpected error while trying to stop the queue.", e);
					Session.get().error(getString("console.maintenance.task.queue.stop.error"));
				}

				FeedbackUtils.refreshFeedback(target, getPage());
			}
		});

		add(new AjaxLink<Void>("start") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!queuedTaskHolderManager.isActive());
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					if (queuedTaskHolderManager.isAvailableForAction()) {
						queuedTaskHolderManager.start();
						getSession().success(getString("console.maintenance.task.queue.start.success"));
					} else {
						getSession().error(getString("console.maintenance.task.queue.action.unavailable"));
					}

					FeedbackUtils.refreshFeedback(target, getPage());
					throw new RestartResponseException(ConsoleMaintenanceTaskListPage.class);
				} catch (RestartResponseException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.error("Unexpected error while trying to start the queue.", e);
					Session.get().error(getString("console.maintenance.task.queue.start.error"));
				}

				FeedbackUtils.refreshFeedback(target, getPage());
			}
		});
	}
}
