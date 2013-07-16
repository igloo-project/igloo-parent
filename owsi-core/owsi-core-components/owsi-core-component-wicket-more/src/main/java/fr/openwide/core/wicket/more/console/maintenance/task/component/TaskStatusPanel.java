package fr.openwide.core.wicket.more.console.maintenance.task.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;

public class TaskStatusPanel extends Panel {

	private static final long serialVersionUID = -7752211078831691283L;

	public TaskStatusPanel(String id, final IModel<TaskStatus> statusModel) {
		super(id);

		WebMarkupContainer status = new WebMarkupContainer("status");
		status.add(new ClassAttributeAppender(new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				switch (statusModel.getObject()) {
				case CANCELLED:
					return "error";
				case COMPLETED:
					return "success";
				case FAILED:
					return "error";
				case INTERRUPTED:
					return "error";
				case RUNNING:
					return "info";
				case TO_RUN:
					return "info";
				default:
					return "";
				}
			}
		}));
		add(status);

		WebMarkupContainer icon = new WebMarkupContainer("icon");
		icon.add(new ClassAttributeAppender(new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				switch (statusModel.getObject()) {
				case CANCELLED:
					return "icon-eject";
				case COMPLETED:
					return "icon-ok";
				case FAILED:
					return "icon-remove";
				case INTERRUPTED:
					return "icon-pause";
				case RUNNING:
					return "icon-repeat";
				case TO_RUN:
					return "icon-play";
				default:
					return "";
				}
			}
		}));
		icon.add(new AttributeAppender("data-original-title", new StringResourceModel(
				"console.maintenance.task.common.status.${}", statusModel)));
		status.add(icon);
	}
}