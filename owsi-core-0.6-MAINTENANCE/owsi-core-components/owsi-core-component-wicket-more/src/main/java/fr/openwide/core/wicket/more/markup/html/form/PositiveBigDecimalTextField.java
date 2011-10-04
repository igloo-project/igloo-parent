package fr.openwide.core.wicket.more.markup.html.form;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.BigDecimalConverter;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.MinimumValidator;

import fr.openwide.core.wicket.markup.html.form.FormComponentHelper;

public class PositiveBigDecimalTextField extends TextField<BigDecimal> {
	private static final long serialVersionUID = 183668115931774497L;

	private static final PositiveBigDecimalConverter DEFAULT_CONVERTER = new PositiveBigDecimalConverter();

	private IConverter customConverter;

	private static final IValidator<BigDecimal> MINIMUM_VALIDATOR = new MinimumValidator<BigDecimal>(BigDecimal.ZERO);

	public PositiveBigDecimalTextField(String id, IModel<BigDecimal> model, String fieldName) {
		this(id, model, fieldName, null);
	}

	public PositiveBigDecimalTextField(String id, IModel<BigDecimal> model, String fieldName, IConverter customConverter) {
		super(id, model, BigDecimal.class);
		add(MINIMUM_VALIDATOR);
		FormComponentHelper.setLabel(this, fieldName);
		
		this.customConverter = customConverter;
	}

	public IConverter getConverter(Class<?> clazz) {
		if (BigDecimal.class.isAssignableFrom(clazz)) {
			if (customConverter != null) {
				return customConverter;
			} else {
				return DEFAULT_CONVERTER;
			}
		} else {
			return super.getConverter(clazz);
		}
	}

	/**
	 * custom converter to disable grouping (thousand separator) in text fields
	 */
	private static class PositiveBigDecimalConverter implements IConverter {

		private static final long serialVersionUID = 5045582390770004920L;

		private static final BigDecimalConverter WICKET_CONVERTER = new BigDecimalConverter();

		@Override
		public Object convertToObject(String value, Locale locale) {
			return WICKET_CONVERTER.convertToObject(value, locale);
		}

		@Override
		public String convertToString(Object value, Locale locale) {
			NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
			numberFormat.setGroupingUsed(false);
			return numberFormat.format((BigDecimal) value);
		}
		
	}
}
