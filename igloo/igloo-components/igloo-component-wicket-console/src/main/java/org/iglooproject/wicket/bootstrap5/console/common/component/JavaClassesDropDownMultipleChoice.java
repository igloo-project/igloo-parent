package org.iglooproject.wicket.bootstrap5.console.common.component;

import java.util.Collection;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

public class JavaClassesDropDownMultipleChoice extends GenericSelect2DropDownMultipleChoice<Class<?>> {

	private static final long serialVersionUID = 8484823009218408585L;

	private static final JavaClassChoiceRenderer JAVA_CLASS_CHOICE_RENDERER = new JavaClassChoiceRenderer();

	public <C extends Collection<Class<?>>> JavaClassesDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier,
		IModel<? extends Collection<? extends Class<?>>> choicesModel
	) {
		super(id, model, collectionSupplier, choicesModel, JAVA_CLASS_CHOICE_RENDERER);
	}

	private static class JavaClassChoiceRenderer extends ChoiceRenderer<Class<?>> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Object getDisplayValue(Class<?> clazz) {
			return clazz != null ? clazz.getSimpleName() : "";
		}
		
		@Override
		public String getIdValue(Class<?> clazz, int index) {
			return clazz != null ? String.valueOf(index) : "-1";
		}
	}

}
