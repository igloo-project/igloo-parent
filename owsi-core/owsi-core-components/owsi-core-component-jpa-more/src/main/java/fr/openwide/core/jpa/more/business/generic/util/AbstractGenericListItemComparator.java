package fr.openwide.core.jpa.more.business.generic.util;

import java.text.Collator;
import java.util.Comparator;

import com.google.common.collect.Ordering;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.util.AbstractGenericEntityComparator;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;

public abstract class AbstractGenericListItemComparator<E extends GenericListItem<?>> extends AbstractGenericEntityComparator<Long, E>{
	
	private static final long serialVersionUID = -6024767658595096844L;
	
	private static final Comparator<Integer> DEFAULT_POSITION_COMPARATOR = Ordering.natural().nullsLast();
	private static final Comparator<String> DEFAULT_LABEL_COMPARATOR = Ordering.from(GenericEntity.DEFAULT_STRING_COLLATOR).nullsLast();
	
	private final Comparator<? super Integer> positionComparator;
	private final Comparator<? super String> labelComparator;
	
	private static final Comparator<? super String> makeNullSafe(Collator collator) {
		return Ordering.from(collator).nullsLast();
	}

	public AbstractGenericListItemComparator() {
		super();
		this.positionComparator = DEFAULT_POSITION_COMPARATOR;
		this.labelComparator = DEFAULT_LABEL_COMPARATOR;
	}

	/**
	 * @param labelCollator The {@link GenericListItem#getLabel() label} collator.
	 * 		Will be wrapped in order to make it consider null elements as higher than non-null elements.
	 * 		Must be serializable in order for this comparator to be serializable.
	 */
	public AbstractGenericListItemComparator(Collator labelCollator) {
		this(makeNullSafe(labelCollator));
	}

	/**
	 * @param labelComparator The {@link GenericListItem#getLabel() label} comparator.
	 * 		Must be null-safe in order for this comparator to be null-safe.
	 * 		Must be serializable in order for this comparator to be serializable.
	 */
	public AbstractGenericListItemComparator(Comparator<? super String> labelComparator) {
		super();
		this.positionComparator = DEFAULT_POSITION_COMPARATOR;
		this.labelComparator = labelComparator;
	}

	/**
	 * @param nullIsLow see {@link AbstractGenericEntityComparator#AbstractGenericEntityComparator(boolean, Comparator)}
	 * @param keyComparator see {@link AbstractGenericEntityComparator#AbstractGenericEntityComparator(boolean, Comparator)}
	 * @param positionComparator The {@link GenericListItem#getPosition() position} comparator.
	 * 		Must be null-safe in order for this comparator to be null-safe.
	 * 		Must be serializable in order for this comparator to be serializable.
	 * @param labelCollator The {@link GenericListItem#getLabel() label} collator.
	 * 		Will be wrapped in order to make it consider null elements as higher than non-null elements.
	 * 		Must be serializable in order for this comparator to be serializable.
	 */
	public AbstractGenericListItemComparator(boolean nullIsLow, Comparator<? super Long> keyComparator,
			Comparator<? super Integer> positionComparator, Collator labelCollator) {
		this(nullIsLow, keyComparator, positionComparator, makeNullSafe(labelCollator));
	}

	/**
	 * @param nullIsLow see {@link AbstractGenericEntityComparator#AbstractGenericEntityComparator(boolean, Comparator)}
	 * @param keyComparator see {@link AbstractGenericEntityComparator#AbstractGenericEntityComparator(boolean, Comparator)}
	 * @param positionComparator The {@link GenericListItem#getPosition() position} comparator.
	 * 		Must be null-safe in order for this comparator to be null-safe.
	 * 		Must be serializable in order for this comparator to be serializable.
	 * @param labelComparator The {@link GenericListItem#getLabel() label} comparator.
	 * 		Must be null-safe in order for this comparator to be null-safe.
	 * 		Must be serializable in order for this comparator to be serializable.
	 */
	public AbstractGenericListItemComparator(boolean nullIsLow, Comparator<? super Long> keyComparator,
			Comparator<? super Integer> positionComparator, Comparator<? super String> labelComparator) {
		super(nullIsLow, keyComparator);
		this.positionComparator = positionComparator;
		this.labelComparator = labelComparator;
	}
	
	@Override
	protected int compareNotNullObjects(E left, E right) {
		// on trie en priorité sur la position, puis sur le libellé et enfin par l'identifiant
		int order = positionComparator.compare(left.getPosition(), right.getPosition());
		
		if (order == 0) {
			order = labelComparator.compare(left.getLabel(), right.getLabel());
		}
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}

}
