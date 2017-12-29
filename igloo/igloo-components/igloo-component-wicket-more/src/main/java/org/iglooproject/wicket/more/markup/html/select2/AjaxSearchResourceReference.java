package org.iglooproject.wicket.more.markup.html.select2;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.util.convert.IConverter;


public abstract class AjaxSearchResourceReference<E> extends org.retzlaff.select2.resource.AjaxSearchResourceReference<E> implements IDetachable {

	private static final long serialVersionUID = -4799279718763482607L;

	public AjaxSearchResourceReference(Class<?> scope, String name, IConverter<E> idToChoiceConverter, IConverter<? super E> renderer) {
		super(scope, name, idToChoiceConverter, renderer);
	}

	@Override
	public void detach() {
		// nothing to do
	}

}
