package fr.openwide.core.wicket.more.console.maintenance.task.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Classes;

import fr.openwide.core.jpa.more.business.task.util.TaskResult;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;

public class TaskResultPanel extends Panel {

	private static final long serialVersionUID = -4365862293922374762L;

	private boolean hideIfEmpty;
	
	private String faSize = "fa-lg";
	
	public TaskResultPanel(String id, final IModel<TaskResult> resultModel) {
		super(id, resultModel);
		
		add(
				new WebMarkupContainer("result") {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						String iconClass = faSize;
						TaskResult result = resultModel.getObject();
						if (result != null) {
							switch (result) {
							case SUCCESS:
								iconClass += " fa-check-circle success";
								break;
							case WARN:
								iconClass += " fa-exclamation-circle warning";
								break;
							case ERROR:
								iconClass += " fa-times-circle danger";
								break;
							case FATAL:
								iconClass += " fa-times-circle-o fail";
								break;
							}
							tag.append("class", iconClass, " ");
							tag.put("title", getString(Classes.simpleName(TaskResult.class) + "." + result.name()));
						}
					}
				}
				.add(Condition.modelNotNull(resultModel).thenShow())
		);
		
		add(
				new EnclosureBehavior() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public boolean isEnabled(Component component) {
						return hideIfEmpty;
					}
					
				}.model(resultModel)
		);
	}
	
	public TaskResultPanel hideIfEmpty() {
		hideIfEmpty = true;
		return this;
	}
	
	public TaskResultPanel faSize(String faSize) {
		this.faSize = faSize;
		return this;
	}
}