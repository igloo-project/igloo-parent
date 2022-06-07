package org.iglooproject.wicket.component;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

public class GenericMarkupContainer<T> extends MarkupContainer implements IGenericComponent<T, GenericMarkupContainer<T>> {

	private static final long serialVersionUID = 6524723170525068340L;

	public GenericMarkupContainer(String id) {
		super(id);
	}
	
	public GenericMarkupContainer(String id, IModel<T> model) {
		super(id, model);
	}

}
