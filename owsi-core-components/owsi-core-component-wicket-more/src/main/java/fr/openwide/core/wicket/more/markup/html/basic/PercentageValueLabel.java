package fr.openwide.core.wicket.more.markup.html.basic;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class PercentageValueLabel extends Label {
	
	private static final long serialVersionUID = -3244126531472735297L;
	
	private static final PercentageConverter PERCENTAGE_CONVERTER = new PercentageConverter("###", true);

	public PercentageValueLabel(String id, IModel<Float> model) {
		super(id, model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (Float.class.isAssignableFrom(type)) {
			return (IConverter<C>) PERCENTAGE_CONVERTER;
		} else {
			return super.getConverter(type);
		}
	}
}
