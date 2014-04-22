package fr.openwide.core.wicket.more.markup.html.select2;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;

public final class DefaultGenericListItemChoiceRenderer implements IChoiceRenderer<GenericListItem<?>> {

	private static final long serialVersionUID = -8375575130669366271L;

	public DefaultGenericListItemChoiceRenderer() { }

	@Override
	public Object getDisplayValue(GenericListItem<?> object) {
		return object == null ? null : object.getLabel();
	}

	@Override
	public String getIdValue(GenericListItem<?> object, int index) {
		return object == null ? null : object.getId().toString();
	}

}
