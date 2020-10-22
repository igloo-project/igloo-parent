package org.iglooproject.wicket.bootstrap4.console.maintenance.task.component;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

public class TaskTypeDropDownMultipleChoice extends GenericSelect2DropDownMultipleChoice<String> {

	private static final long serialVersionUID = 3147073422245294521L;

	public <C extends Collection<String>> TaskTypeDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier
	) {
		this(id, model, collectionSupplier, new ChoicesModel());
	}

	public <C extends Collection<String>> TaskTypeDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier,
		IModel<? extends Collection<? extends String>> choicesModel
	) {
		super(id, model, collectionSupplier, choicesModel, new ChoiceRenderer<>());
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
