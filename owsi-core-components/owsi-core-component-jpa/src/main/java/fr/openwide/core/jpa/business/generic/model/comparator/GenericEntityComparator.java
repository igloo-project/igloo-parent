package fr.openwide.core.jpa.business.generic.model.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.util.Assert;

import com.google.common.collect.Ordering;

import fr.openwide.core.commons.util.ordering.AbstractNullSafeComparator;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * A {@link GenericEntity} comparator that compares IDs.
 * <p>The {@link GenericEntityComparator default implementation} mimics the behavior of {@link GenericEntity#compareTo(GenericEntity)}, except that it is
 * null-safe, both when comparing entities and when comparing their IDs. It assumes a null entity is less than a non-null one, and that an entity with a null ID
 * is less than an entity with a non-null ID.
 * <p>This comparator is consistent with equals.
 */
public class GenericEntityComparator<K extends Comparable<K> & Serializable, E extends GenericEntity<K,E>> extends AbstractNullSafeComparator<E> {

	private static final long serialVersionUID = -5933751018161012653L;
	
	private final Comparator<? super K> KEY_COMPARATOR;

	/**
	 *  
	 */
	public GenericEntityComparator() {
		this(true, Ordering.natural().nullsFirst());
	}

	/**
	 * @param nullIsLow Whether a null entity is lower than a non-null entity. This only applies to the entity object, not to its id.
	 * @param keyComparator The key comparator.
	 * 		Must be null-safe in order for this comparator to be null-safe.
	 * 		Must be serializable in order for this comparator to be serializable.
	 */
	public GenericEntityComparator(boolean nullIsLow, Comparator<? super K> keyComparator) {
		super(nullIsLow);
		Assert.notNull(keyComparator);
		this.KEY_COMPARATOR = keyComparator;
	}
	
	@Override
	protected boolean equalsNotNullObjects(E left, E right) {
		return left.equals(right);
	}

	@Override
	protected int compareNotNullObjects(E left, E right) {
		return KEY_COMPARATOR.compare(left.getId(), right.getId());
	}

}
