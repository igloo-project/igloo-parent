package fr.openwide.core.wicket.more.model;

import java.io.Serializable;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ReadOnlyModel<T> extends AbstractReadOnlyModel<T> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends T> wrappedModel;
	
	public static <T> ReadOnlyModel<T> of(IModel<? extends T> model) {
		return new ReadOnlyModel<T>(model);
	}

	public static <T extends Serializable> ReadOnlyModel<T> of(T object) {
		return new ReadOnlyModel<T>(Model.of(object));
	}

	protected ReadOnlyModel(IModel<? extends T> wrappedModel) {
		super();
		this.wrappedModel = wrappedModel;
	}

	@Override
	public T getObject() {
		return wrappedModel.getObject();
	}
	
	@Override
	public void detach() {
		super.detach();
		wrappedModel.detach();
	}

}
