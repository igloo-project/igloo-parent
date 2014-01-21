package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.Set;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityHashSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, Set<E>> {
	
	private static final long serialVersionUID = 7391021833949792344L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityHashSetModel<K, E> of(Class<E> clazz) {
		return new GenericEntityHashSetModel<K, E>(clazz);
	}

	protected GenericEntityHashSetModel(Class<E> clazz) {
		super(clazz, Suppliers2.<E>hashSet());
	}

}
