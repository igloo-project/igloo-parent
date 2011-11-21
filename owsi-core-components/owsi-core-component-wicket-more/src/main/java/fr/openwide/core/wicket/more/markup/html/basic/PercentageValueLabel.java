package fr.openwide.core.wicket.more.markup.html.basic;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.more.util.NumberAdapter;
import fr.openwide.core.wicket.more.util.convert.converters.PercentageConverter;

public class PercentageValueLabel<N extends Number> extends Label {
	private static final long serialVersionUID = -3244126531472735297L;

	private final NumberAdapter<N> numberAdapter;

	private final PercentageConverter<N> percentageConverter;

	public PercentageValueLabel(String id, IModel<? extends Number> model, NumberAdapter<N> numberAdapter) {
		this(id, model, numberAdapter, true);
	}

	public PercentageValueLabel(String id, IModel<? extends Number> model, NumberAdapter<N> numberAdapter, boolean displayPercentSymbol) {
		super(id, model);
		
		this.numberAdapter = numberAdapter;
		this.percentageConverter = new PercentageConverter<N>("###", numberAdapter, displayPercentSymbol);
	}

	@Override
	public IConverter getConverter(Class<?> type) {
		if (numberAdapter.getNumberClass().isAssignableFrom(type)) {
			return percentageConverter;
		} else {
			return super.getConverter(type);
		}
	}
}
