package fr.openwide.core.wicket.more.markup.html.form.impl;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;

public final class GenericListItemChoiceRenderer implements IChoiceRenderer<GenericListItem<?>> {
	
	private static final long serialVersionUID = 7703341197679633131L;
	
	private static GenericListItemChoiceRenderer INSTANCE = new GenericListItemChoiceRenderer();
	public static GenericListItemChoiceRenderer get() {
		return INSTANCE;
	}
	
	private GenericListItemChoiceRenderer() { }
	
	@Override
	public Object getDisplayValue(GenericListItem<?> object) {
		if (object == null) {
			return null;
		}
		return object.getLabel();
	}
	
	@Override
	public String getIdValue(GenericListItem<?> object, int index) {
		if (object == null) {
			return null;
		}
		return object.getId().toString();
	}
}
