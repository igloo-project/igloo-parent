package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.Set;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class SessionThreadSafeGenericEntityHashSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractSessionThreadSafeGenericEntityCollectionModel<K, E, Set<E>> {
	
	private static final long serialVersionUID = -3618904709308977111L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			SessionThreadSafeGenericEntityHashSetModel<K, E> of(Class<E> clazz) {
		return new SessionThreadSafeGenericEntityHashSetModel<K, E>(clazz);
	}

	public SessionThreadSafeGenericEntityHashSetModel(Class<E> clazz) {
		super(clazz, Suppliers2.<E>hashSet());
	}

}
