package fr.openwide.core.wicket.more.console.maintenance.task.component;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.spring.config.CoreConfigurer;

public class TaskQueueIdListMultipleChoice extends ListMultipleChoice<String> {

	private static final long serialVersionUID = 3147073422245294521L;
	
	private static final String DEFAULT_QUEUE_ID_VALUE = "&NULL";

	public TaskQueueIdListMultipleChoice(String id, IModel<? extends Collection<String>> typeListModel) {
		super(id, typeListModel, new QueueIdsChoicesModel());
		setChoiceRenderer(new QueueIdChoiceRenderer());
	}

	private static class QueueIdsChoicesModel extends LoadableDetachableModel<List<String>> {
		private static final long serialVersionUID = 1L;

		@SpringBean
		private CoreConfigurer configurer;
		
		public QueueIdsChoicesModel() {
			Injector.get().inject(this);
		}
		
		@Override
		protected List<String> load() {
			List<String> queueIds = configurer.getTaskQueueIds();
			queueIds.add(0, null);
			return queueIds;
		}
	}
	
	private class QueueIdChoiceRenderer implements IChoiceRenderer<String> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Object getDisplayValue(String object) {
			if (object == null) {
				return getString("console.maintenance.task.common.queue.default");
			}
			return object;
		}
		
		@Override
		public String getIdValue(String object, int index) {
			if (object == null) {
				return DEFAULT_QUEUE_ID_VALUE;
			}
			return object;
		}
		
	}
}
