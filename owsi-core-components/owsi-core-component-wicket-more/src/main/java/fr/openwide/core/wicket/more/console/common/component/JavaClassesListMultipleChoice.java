package fr.openwide.core.wicket.more.console.common.component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

public class JavaClassesListMultipleChoice extends ListMultipleChoice<Class<?>> {

	private static final long serialVersionUID = 1921653943974992253L;

	private static final JavaClassChoiceRenderer JAVA_CLASS_CHOICE_RENDERER = new JavaClassChoiceRenderer();

	public JavaClassesListMultipleChoice(String id, IModel<? extends Collection<Class<?>>> classListModel,
			Set<? extends Class<?>> choicesModel) {
		this(id, classListModel, Lists.newArrayList(choicesModel));
	}

	public JavaClassesListMultipleChoice(String id, IModel<? extends Collection<Class<?>>> classListModel,
			List<? extends Class<?>> choicesModel) {
		super(id, classListModel, choicesModel, JAVA_CLASS_CHOICE_RENDERER);
	}

	private static class JavaClassChoiceRenderer implements IChoiceRenderer<Class<?>> {
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
