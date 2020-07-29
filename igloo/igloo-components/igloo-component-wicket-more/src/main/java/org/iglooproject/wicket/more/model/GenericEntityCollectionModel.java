package org.iglooproject.wicket.more.model;

import java.io.Serializable;
import java.util.Collection;

import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class GenericEntityCollectionModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, Collection<E>> {

	private static final long serialVersionUID = -2960630322941056821L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> GenericEntityCollectionModel<K, E> of(
			Class<E> clazz) {
		return new GenericEntityCollectionModel<>(clazz, Suppliers2.<E>arrayList());
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> GenericEntityCollectionModel<K, E> of(
			Class<E> clazz, SerializableSupplier2<? extends Collection<E>> collectionSupplier) {
		return new GenericEntityCollectionModel<>(clazz, collectionSupplier);
	}

	protected GenericEntityCollectionModel(Class<E> clazz, SerializableSupplier2<? extends Collection<E>> collectionSupplier) {
		super(clazz, collectionSupplier);
	}

}
