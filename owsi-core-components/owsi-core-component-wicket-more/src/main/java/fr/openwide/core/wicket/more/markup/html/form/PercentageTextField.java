package fr.openwide.core.wicket.more.markup.html.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.markup.html.form.FormComponentHelper;
import fr.openwide.core.wicket.more.util.NumberAdapter;
import fr.openwide.core.wicket.more.util.convert.converters.PercentageConverter;
import fr.openwide.core.wicket.more.util.validator.PercentageValidator;

public class PercentageTextField<N extends Number & Comparable<N>> extends TextField<N> {
	private static final long serialVersionUID = -3071860178961793589L;

	private final NumberAdapter<N> numberAdapter;

	private final PercentageConverter<N> percentageConverter;

	public PercentageTextField(String id, IModel<N> model, NumberAdapter<N> numberAdapter, String fieldName) {
		this(id, model, numberAdapter, fieldName, false);
	}

	public PercentageTextField(String id, IModel<N> model, NumberAdapter<N> numberAdapter, String fieldName, boolean displayPercentSymbol) {
		super(id, model, numberAdapter.getNumberClass());
		this.numberAdapter = numberAdapter;
		this.percentageConverter = new PercentageConverter<N>("###", numberAdapter, displayPercentSymbol);
		add(new PercentageValidator<N>(numberAdapter));
		
		FormComponentHelper.setLabel(this, fieldName);
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
