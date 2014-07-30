package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.LinkedHashSet;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityLinkedHashSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, LinkedHashSet<E>> {
	
	private static final long serialVersionUID = 7391021833949792344L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityLinkedHashSetModel<K, E> of(Class<E> clazz) {
		return new GenericEntityLinkedHashSetModel<K, E>(clazz);
	}

	protected GenericEntityLinkedHashSetModel(Class<E> clazz) {
		super(clazz, Suppliers2.<E>linkedHashSet());
	}

}
