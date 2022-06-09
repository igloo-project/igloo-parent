package org.iglooproject.wicket.more.markup.html.form;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.RangeValidator;

import igloo.wicket.markup.html.form.FormComponentHelper;

public class PositiveBigDecimalTextField extends TextField<BigDecimal> {
	private static final long serialVersionUID = 183668115931774497L;

	private static final PositiveBigDecimalConverter DEFAULT_CONVERTER = new PositiveBigDecimalConverter();

	private IConverter<BigDecimal> customConverter;

	private static final IValidator<BigDecimal> MINIMUM_VALIDATOR = RangeValidator.minimum(BigDecimal.ZERO);

	public PositiveBigDecimalTextField(String id, IModel<BigDecimal> model, String fieldName) {
		this(id, model, fieldName, null);
	}

	public PositiveBigDecimalTextField(String id, IModel<BigDecimal> model, String fieldName,
			IConverter<BigDecimal> customConverter) {
		super(id, model, BigDecimal.class);
		add(MINIMUM_VALIDATOR);
		FormComponentHelper.setLabel(this, fieldName);
		
		this.customConverter = customConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (BigDecimal.class.isAssignableFrom(type)) {
			if (customConverter != null) {
				return (IConverter<C>) customConverter;
			} else {
				return (IConverter<C>) DEFAULT_CONVERTER;
			}
		} else {
			return super.getConverter(type);
		}
	}

	/**
	 * custom converter to disable grouping (thousand separator) in text fields
	 */
	private static class PositiveBigDecimalConverter implements IConverter<BigDecimal> {

		private static final long serialVersionUID = 5045582390770004920L;

		private static final BigDecimalConverter WICKET_CONVERTER = new BigDecimalConverter();

		@Override
		public BigDecimal convertToObject(String value, Locale locale) {
			return WICKET_CONVERTER.convertToObject(value, locale);
		}

		@Override
		public String convertToString(BigDecimal value, Locale locale) {
			NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
			numberFormat.setGroupingUsed(false);
			return numberFormat.format((BigDecimal) value);
		}
		
	}
}
