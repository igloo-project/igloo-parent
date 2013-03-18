package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityHashSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends GenericEntityCollectionModel<K, E, Set<E>> {
	
	private static final long serialVersionUID = 7391021833949792344L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityHashSetModel<K, E> of(Class<E> clazz) {
		return new GenericEntityHashSetModel<K, E>(clazz);
	}

	protected GenericEntityHashSetModel(Class<E> clazz) {
		super(clazz, Sets.<E>newHashSet());
	}

	@Override
	protected Set<E> createEntityCollection() {
		return Sets.newHashSet();
	}

}
