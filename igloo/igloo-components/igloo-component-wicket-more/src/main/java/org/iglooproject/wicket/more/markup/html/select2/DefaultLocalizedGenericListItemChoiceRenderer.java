package org.iglooproject.wicket.more.markup.html.select2;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

import org.iglooproject.jpa.more.business.generic.model.GenericLocalizedGenericListItem;

public class DefaultLocalizedGenericListItemChoiceRenderer extends ChoiceRenderer<GenericLocalizedGenericListItem<?, ?>> {

	private static final long serialVersionUID = -324543602711731900L;
	
	public DefaultLocalizedGenericListItemChoiceRenderer() { }

	@Override
	public Object getDisplayValue(GenericLocalizedGenericListItem<?, ?> object) {
		return object == null ? null : object.getLabel().get(getLocale());
	}

	@Override
	public String getIdValue(GenericLocalizedGenericListItem<?, ?> object, int index) {
		return object == null ? null : object.getId().toString();
	}
	
	protected Locale getLocale() {
		return Session.get().getLocale();
	}

}
