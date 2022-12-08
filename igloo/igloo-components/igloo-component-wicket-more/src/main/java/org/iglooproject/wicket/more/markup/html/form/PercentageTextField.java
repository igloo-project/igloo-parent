package org.iglooproject.wicket.more.markup.html.form;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import igloo.wicket.component.PercentageBigDecimalConverter;
import igloo.wicket.component.PercentageValidator;
import igloo.wicket.markup.html.form.FormComponentHelper;

public class PercentageTextField extends TextField<BigDecimal> {
	private static final long serialVersionUID = -3071860178961793589L;

	private static final int DEFAULT_SCALE = 2;

	private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

	private static final int DEFAULT_MAX_PRECISION = 6;

	private boolean modelIsRatio;

	public PercentageTextField(String id, IModel<BigDecimal> model, String fieldName) {
		this(id, model, fieldName, true);
	}

	public PercentageTextField(String id, IModel<BigDecimal> model, String fieldName, boolean modelIsRatio) {
		super(id, model, BigDecimal.class);
		this.modelIsRatio = modelIsRatio;
		
		FormComponentHelper.setLabel(this, fieldName);
		addValidator();
		
		if (getRoundingMode() == null) {
			throw new IllegalArgumentException("The roundingMode parameter must not be null.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (BigDecimal.class.isAssignableFrom(type)) {
			return (IConverter<C>) new PercentageBigDecimalConverter(
					getScale(), getRoundingMode(), getMaxPrecision(), modelIsRatio);
		} else {
			return super.getConverter(type);
		}
	}

	protected void addValidator() {
		if (modelIsRatio) {
			this.add(PercentageValidator.getDefaultPercentageAsRatioValidator());
		} else {
			this.add(PercentageValidator.getDefaultPercentageValidator());
		}
	}

	protected int getScale() {
		return DEFAULT_SCALE;
	}

	protected RoundingMode getRoundingMode() {
		return DEFAULT_ROUNDING_MODE;
	}

	protected int getMaxPrecision() {
		return DEFAULT_MAX_PRECISION;
	}

}
