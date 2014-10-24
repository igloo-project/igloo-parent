package fr.openwide.core.wicket.more.console.maintenance.task.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Classes;

import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;

public class TaskStatusPanel extends GenericPanel<TaskStatus> {

	private static final long serialVersionUID = -7752211078831691283L;

	private boolean hideIfEmpty;
	
	public TaskStatusPanel(String id, final IModel<TaskStatus> statusModel) {
		super(id, statusModel);
		
		add(
				new WebMarkupContainer("status") {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						String iconClass = null;
						TaskStatus status = statusModel.getObject();
						if (status != null) {
							switch (status) {
								case TO_RUN:
									iconClass = "fa-play-circle info";
									break;
								case RUNNING:
									iconClass = "fa-repeat info";
									break;
								case COMPLETED:
									iconClass = "fa-check-circle success";
									break;
								case FAILED:
									iconClass = "fa-times-circle fail";
									break;
								case INTERRUPTED:
									iconClass = "fa-pause fail";
									break;
								case CANCELLED:
									iconClass = "fa-ban fail";
									break;
								default:
									iconClass = "";
									break;
							}
							tag.append("class", iconClass, " ");
							tag.put("title", getString(Classes.simpleName(TaskStatus.class) + "." + status.name()));
						}
					}
				}
				.add(new EnclosureBehavior().model(statusModel))
		);
		
		add(
				new EnclosureBehavior() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public boolean isEnabled(Component component) {
						return hideIfEmpty;
					}
					
				}.model(statusModel)
		);
	}
	
	public TaskStatusPanel hideIfEmpty() {
		hideIfEmpty = true;
		return this;
	}
}