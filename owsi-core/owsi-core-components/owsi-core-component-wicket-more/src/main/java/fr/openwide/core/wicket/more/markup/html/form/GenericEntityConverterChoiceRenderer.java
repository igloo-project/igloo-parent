package fr.openwide.core.wicket.more.markup.html.form;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityConverterChoiceRenderer<T extends GenericEntity<?, ?>> extends ChoiceRenderer<T> {
	
	private static final long serialVersionUID = -8949718525256769535L;

	private final IConverter<? super T> converter;

	public GenericEntityConverterChoiceRenderer(IConverter<? super T> converter) {
		super();
		this.converter = converter;
	}

	@Override
	public Object getDisplayValue(T object) {
		return converter.convertToString(object, getLocale());
	}

	@Override
	public String getIdValue(T object, int index) {
		return object == null ? null : String.valueOf(object.getId());
	}
	
	protected Locale getLocale() {
		return Session.get().getLocale();
	}

}
