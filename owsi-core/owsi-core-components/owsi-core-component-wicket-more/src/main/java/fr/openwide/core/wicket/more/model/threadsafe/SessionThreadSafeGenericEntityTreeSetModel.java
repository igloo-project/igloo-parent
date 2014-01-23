package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class SessionThreadSafeGenericEntityTreeSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractSessionThreadSafeGenericEntityCollectionModel<K, E, SortedSet<E>> {
	
	private static final long serialVersionUID = -3618904709308977111L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			SessionThreadSafeGenericEntityTreeSetModel<K, E> of(Class<E> clazz) {
		return new SessionThreadSafeGenericEntityTreeSetModel<K, E>(clazz, Suppliers2.<E>treeSet());
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			SessionThreadSafeGenericEntityTreeSetModel<K, E> of(Class<E> clazz, Comparator<? super E> comparator) {
		return new SessionThreadSafeGenericEntityTreeSetModel<K, E>(clazz, Suppliers2.<E>treeSet(comparator));
	}

	public SessionThreadSafeGenericEntityTreeSetModel(Class<E> clazz, Supplier<? extends TreeSet<E>> newCollectionSupplier) {
		super(clazz, newCollectionSupplier);
	}

}
