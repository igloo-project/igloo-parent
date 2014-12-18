package fr.openwide.core.wicket.more.markup.html.basic;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class PercentageValueLabel extends Label {

	private static final long serialVersionUID = -3244126531472735297L;

	private static final int DEFAULT_SCALE = 2;

	private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

	private static final int DEFAULT_MAX_PRECISION = 6;

	private boolean modelIsRatio;

	private boolean displayPercentSymbol;

	public PercentageValueLabel(String id, IModel<BigDecimal> model, boolean modelIsRatio, boolean displayPercentSymbol) {
		super(id, model);
		
		if (getRoundingMode() == null) {
			throw new IllegalArgumentException("The roundingMode parameter must not be null.");
		}
		
		this.modelIsRatio = modelIsRatio;
		this.displayPercentSymbol = displayPercentSymbol;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (BigDecimal.class.isAssignableFrom(type)) {
			return (IConverter<C>) new PercentageBigDecimalConverter(
					getScale(), getRoundingMode(), getMaxPrecision(), modelIsRatio, displayPercentSymbol);
		} else {
			return super.getConverter(type);
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
