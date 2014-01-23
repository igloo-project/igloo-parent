package fr.openwide.core.wicket.more.console.maintenance.task.component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

public class TaskStatusListMultipleChoice extends ListMultipleChoice<TaskStatus> {

	private static final long serialVersionUID = 3147073422245294521L;

	public TaskStatusListMultipleChoice(String id, IModel<? extends Collection<TaskStatus>> statusListModel) {
		super(id, statusListModel, new LoadableDetachableModel<List<TaskStatus>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<TaskStatus> load() {
				return Arrays.asList(TaskStatus.values());
			}
		});
		setChoiceRenderer(new TaskStatusChoiceRenderer());
	}

	private class TaskStatusChoiceRenderer implements IChoiceRenderer<TaskStatus> {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(final TaskStatus object) {
			return object != null ? new StringResourceModel("console.maintenance.task.common.status.${}",
					TaskStatusListMultipleChoice.this, Model.of(object.name())).getString() : "";
		}

		@Override
		public String getIdValue(TaskStatus object, int index) {
			return object != null ? String.valueOf(index) : "-1";
		}
	}
}
