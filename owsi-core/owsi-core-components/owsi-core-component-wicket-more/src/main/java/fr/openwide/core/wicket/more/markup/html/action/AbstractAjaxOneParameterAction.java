package fr.openwide.core.wicket.more.markup.html.action;

import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;

public abstract class AbstractAjaxOneParameterAction<T> implements IAjaxOneParameterAjaxAction<T> {

	private static final long serialVersionUID = 3044339655373492663L;

	@Override
	public void updateAjaxAttributes(AjaxRequestAttributes attributes, T model) {
		// nothing to do
	}

	@Override
	public void detach() {
		// nothing to do
	}

}
