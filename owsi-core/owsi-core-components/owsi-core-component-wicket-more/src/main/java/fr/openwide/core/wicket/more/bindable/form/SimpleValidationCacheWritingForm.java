package fr.openwide.core.wicket.more.bindable.form;

import org.apache.wicket.bean.validation.GroupsModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.bindable.model.IBindableModel;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractDetachableFactory;

public class SimpleValidationCacheWritingForm<M extends IBindableModel<T>, T> extends ValidationCacheWritingForm<M, T, T> {

	private static final long serialVersionUID = -5510043743942308327L;

	public SimpleValidationCacheWritingForm(String id, M bindableModel) {
		this(id, bindableModel, new GroupsModel());
	}

	public SimpleValidationCacheWritingForm(String id, M bindableModel, IModel<Class<?>[]> groupsModel) {
		super(
				id,
				bindableModel,
				new AbstractDetachableFactory<M, T>() {
					private static final long serialVersionUID = 1L;
					@Override
					public T create(M parameter) {
						return parameter.getObject();
					}
				},
				groupsModel
		);
	}

}