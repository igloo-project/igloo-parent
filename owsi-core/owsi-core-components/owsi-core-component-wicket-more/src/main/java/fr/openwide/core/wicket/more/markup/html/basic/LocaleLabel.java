package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.markup.html.basic.GenericLabel;
import fr.openwide.core.wicket.more.rendering.LocaleRenderer;

public class LocaleLabel extends GenericLabel<Locale> {

	private static final long serialVersionUID = 8270747942276592901L;

	public LocaleLabel(String id, IModel<Locale> model) {
		super(id, model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (Locale.class.isAssignableFrom(type)) {
			return (IConverter<C>) LocaleRenderer.get();
		} else {
			return super.getConverter(type);
		}
	}

}
