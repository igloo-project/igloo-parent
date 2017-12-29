package org.iglooproject.showcase.web.application.task.component;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.showcase.core.business.task.model.TaskTypeEnum;
import org.iglooproject.showcase.web.application.task.model.TaskDataProvider;
import org.iglooproject.wicket.markup.html.form.PageableSearchForm;
import org.iglooproject.wicket.more.markup.html.form.DatePicker;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.util.DatePattern;

public class TaskSearchPanel extends Panel {
	
	private static final long serialVersionUID = -6224313886789870489L;
	
	private final TaskDataProvider dataProvider;
	
	public TaskSearchPanel(String id, IPageable pageable, TaskDataProvider dataProvider) {
		super(id);
		this.dataProvider = dataProvider;
		
		// Search form
		PageableSearchForm<Void> form = new PageableSearchForm<Void>("form", pageable);
		form.add(
				new ShowcaseTaskQueueIdDropDownChoice("queueId", dataProvider.getQueueIdModel())
						.setLabel(new ResourceModel("tasks.queue"))
						.add(new LabelPlaceholderBehavior()),
				new EnumDropDownSingleChoice<TaskTypeEnum>("type", dataProvider.getTypeModel(), TaskTypeEnum.class)
						.setLabel(new ResourceModel("tasks.type"))
						.add(new LabelPlaceholderBehavior()),
				new TextField<String>("name", dataProvider.getNameModel())
						.setLabel(new ResourceModel("tasks.name"))
						.add(new LabelPlaceholderBehavior()),
				new EnumDropDownSingleChoice<TaskStatus>("status", dataProvider.getStatusModel(), TaskStatus.class)
						.setLabel(new ResourceModel("tasks.status"))
						.add(new LabelPlaceholderBehavior()),
				new EnumDropDownSingleChoice<TaskResult>("result", dataProvider.getResultModel(), TaskResult.class)
						.setLabel(new ResourceModel("tasks.result"))
						.add(new LabelPlaceholderBehavior()),
				new DatePicker("dateMin", dataProvider.getDateMinModel(), DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("tasks.dateMin"))
						.add(new LabelPlaceholderBehavior()),
				new DatePicker("dateMax", dataProvider.getDateMaxModel(), DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("tasks.dateMax"))
						.add(new LabelPlaceholderBehavior())
				
		);
		add(form);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		dataProvider.detach();
	}
}
