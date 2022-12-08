package org.iglooproject.wicket.more.model;

import org.apache.wicket.model.IModel;

import com.google.common.collect.Range;

import igloo.wicket.model.Detachables;

/**
 * A range model representing an interval between two values.
 */
public class RangeModel<C extends Comparable<?>> implements IModel<Range<C>> {

	private static final long serialVersionUID = 2798667461907515108L;
	
	/**
	 * <p>Returns a point, represented by a closed range model in which the lower and the upper bounds are equal.
	 */
	public static <C extends Comparable<?>> RangeModel<C> singleton(IModel<? extends C> pointModel) {
		return new RangeModel<>(pointModel, pointModel);
	}
	
	/**
	 * <p>Returns a closed range model bounded by the values contained in the given 
	 * {@code lowerBoundModel} and {@code upperBoundModel}.
	 * 
	 * @throws IllegalArgumentException 
	 *           if the value of {@code lowerBoundModel} is greater than the value of {@code upperBoundModel}
	 */
	public static <C extends Comparable<?>> RangeModel<C> closed(IModel<? extends C> lowerBoundModel, IModel<? extends C> upperBoundModel) {
		return new RangeModel<>(lowerBoundModel, upperBoundModel);
	}
	
	/**
	 * <p>Returns a right-closed left-unbounded range model bounded by the value contained in the given 
	 * {@code upperBoundModel}.
	 */
	public static <C extends Comparable<?>> RangeModel<C> atMost(IModel<? extends C> upperBoundModel) {
		return new RangeModel<>(null, upperBoundModel);
	}
	
	/**
	 * <p>Returns a left-closed right-unbounded range model bounded by the value contained in the given 
	 * {@code lowerBoundModel}.
	 */
	public static <C extends Comparable<?>> RangeModel<C> atLeast(IModel<? extends C> lowerBoundModel) {
		return new RangeModel<>(lowerBoundModel, null);
	}
	
	private final IModel<? extends C> lowerBoundModel;
	private final IModel<? extends C> upperBoundModel;
	
	/**
	 * Constructs a closed range model from a lower and an upper bound.
	 * 
	 * @param lowerBoundModel
	 *         the lower bound of this range, or null for negative infinity
	 * @param upperBoundModel
	 *         the upper bound of this range, or null for positive infinity
	 */
	private RangeModel(IModel<? extends C> lowerBoundModel, IModel<? extends C> upperBoundModel) {
		super();
		this.lowerBoundModel = lowerBoundModel;
		this.upperBoundModel = upperBoundModel;
	}
	
	@Override
	public Range<C> getObject() {
		if ((lowerBoundModel == null || lowerBoundModel.getObject() == null)
				&& (upperBoundModel == null || upperBoundModel.getObject() == null)) {
			return null;
		} else if (lowerBoundModel == null || lowerBoundModel.getObject() == null) {
			return Range.<C>atMost(upperBoundModel.getObject());
		} else if (upperBoundModel == null || upperBoundModel.getObject() == null) {
			return Range.<C>atLeast(lowerBoundModel.getObject());
		} else {
			return Range.<C>closed(lowerBoundModel.getObject(), upperBoundModel.getObject());
		}
	}
	
	@Override
	public void detach() {
		IModel.super.detach();
		Detachables.detach(lowerBoundModel, upperBoundModel);
	}
}
