package org.iglooproject.wicket.bootstrap3.console.maintenance.task.component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Classes;
import org.iglooproject.jpa.more.business.task.util.TaskResult;

public class TaskResultListMultipleChoice extends ListMultipleChoice<TaskResult> {

	private static final long serialVersionUID = -6536121467575076892L;

	public TaskResultListMultipleChoice(String id, IModel<? extends Collection<TaskResult>> statusListModel) {
		super(id, statusListModel, new LoadableDetachableModel<List<TaskResult>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<TaskResult> load() {
				return Arrays.asList(TaskResult.values());
			}
		});
		setChoiceRenderer(new TaskResultChoiceRenderer());
	}

	private class TaskResultChoiceRenderer extends ChoiceRenderer<TaskResult> {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(final TaskResult object) {
			return object != null ? new StringResourceModel(Classes.simpleName(TaskResult.class) + ".${}",
					TaskResultListMultipleChoice.this, Model.of(object.name())).getString() : "";
		}

		@Override
		public String getIdValue(TaskResult object, int index) {
			return object != null ? String.valueOf(index) : "-1";
		}
	}
}
