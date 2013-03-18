package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityArrayListModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends GenericEntityCollectionModel<K, E, List<E>> {
	
	private static final long serialVersionUID = 7130088291928970625L;
	
	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityArrayListModel<K, E> of(Class<E> clazz) {
		return new GenericEntityArrayListModel<K, E>(clazz);
	}

	protected GenericEntityArrayListModel(Class<E> clazz) {
		super(clazz, Lists.<E>newArrayList());
	}

	@Override
	protected List<E> createEntityCollection() {
		return Lists.newArrayList();
	}

}
