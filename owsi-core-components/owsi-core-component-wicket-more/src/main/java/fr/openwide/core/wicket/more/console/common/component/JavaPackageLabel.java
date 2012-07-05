package fr.openwide.core.wicket.more.console.common.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.more.console.common.converter.JavaPackageReadOnlyConverter;

public class JavaPackageLabel extends Label {

	private static final long serialVersionUID = -835440504309477338L;
	
	private static final JavaPackageReadOnlyConverter CONVERTER = new JavaPackageReadOnlyConverter();
	
	public JavaPackageLabel(String id, IModel<String> packageNameModel) {
		super(id, packageNameModel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (String.class.isAssignableFrom(type)) {
			return (IConverter<C>) CONVERTER;
		} else {
			return super.getConverter(type);
		}
	}

}
