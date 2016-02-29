package fr.openwide.core.wicket.more.model;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Range;

import fr.openwide.core.wicket.more.util.model.Detachables;

/**
 * Creates a range model representing an interval between two values.
 */
public class RangeModel<C extends Comparable<?>> extends AbstractReadOnlyModel<Range<C>> {

	private static final long serialVersionUID = 2798667461907515108L;
	
	private final IModel<C> lowerBoundModel;
	private final IModel<C> upperBoundModel;
	
	/**
	 * Constructs a closed range model from a lower and an upper bound.
	 * 
	 * @param lowerBoundModel
	 *         the lower bound of this range, or null for negative infinity
	 * @param upperBoundModel
	 *         the upper bound of this range, or null for positive infinity
	 */
	public RangeModel(IModel<C> lowerBoundModel, IModel<C> upperBoundModel) {
		super();
		// NOTE Penser à surcharger avec des boolean si besoin de manipuler des intervalles ouverts / semi-ouverts.
		this.lowerBoundModel = lowerBoundModel;
		this.upperBoundModel = upperBoundModel;
	}
	
	/**
	 * Constructs a closed range model representing a point where the lower bound and the upper bound are equal.
	 * 
	 * @param pointModel
	 *         the point representing both the lower and the upper bounds
	 */
	public RangeModel(IModel<C> pointModel) {
		this(pointModel, pointModel);
	}
	
	@Override
	public Range<C> getObject() {
		// NOTE Penser à surcharger avec des boolean si besoin de manipuler des intervalles ouverts / semi-ouverts.
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
		super.detach();
		Detachables.detach(lowerBoundModel, upperBoundModel);
	}
}
