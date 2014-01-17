package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.SortedSet;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityTreeSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, SortedSet<E>> {

	private static final long serialVersionUID = -1197465312043104726L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityTreeSetModel<K, E> of(Class<E> clazz) {
		return new GenericEntityTreeSetModel<K, E>(clazz);
	}

	public GenericEntityTreeSetModel(Class<E> clazz) {
		super(clazz);
	}

	@Override
	protected SortedSet<E> createEntityCollection() {
		return Sets.newTreeSet();
	}

}
