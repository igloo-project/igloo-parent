package fr.openwide.core.wicket.more.markup.html.form;


import org.apache.wicket.markup.html.form.ChoiceRenderer;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public abstract class AbstractGenericEntityChoiceRenderer<E extends GenericEntity<?, ?>>
		extends ChoiceRenderer<E> {
	
	private static final long serialVersionUID = 644472424513953048L;
	
	@Override
	public String getIdValue(E object, int index) {
		return object != null ? object.getId().toString() : "-1";
	}
}
