package fr.openwide.core.wicket.more.console.maintenance.task.component;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;

public class TaskTypeListMultipleChoice extends ListMultipleChoice<String> {

	private static final long serialVersionUID = 3147073422245294521L;

	public TaskTypeListMultipleChoice(String id, IModel<? extends Collection<String>> typeListModel) {
		super(id, typeListModel, new ChoicesModel(), new ChoiceRenderer<String>());
	}

	private static class ChoicesModel extends LoadableDetachableModel<List<String>> {
		private static final long serialVersionUID = 1L;

		@SpringBean
		private IQueuedTaskHolderService queuedTaskHolderService;

		public ChoicesModel() {
			Injector.get().inject(this);
		}

		@Override
		protected List<String> load() {
			return queuedTaskHolderService.listTypes();
		}
	}
}
