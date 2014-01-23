package fr.openwide.core.wicket.more.markup.html.basic;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

public class GenericMarkupContainer<T> extends MarkupContainer implements IGenericComponent<T> {

	private static final long serialVersionUID = 6524723170525068340L;

	public GenericMarkupContainer(String id) {
		super(id);
	}
	
	public GenericMarkupContainer(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void setModel(IModel<T> model) {
		setDefaultModel(model);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T getModelObject() {
		return (T) getDefaultModelObject();
	}

	@Override
	public void setModelObject(T object) {
		setDefaultModelObject(object);
	}
}
