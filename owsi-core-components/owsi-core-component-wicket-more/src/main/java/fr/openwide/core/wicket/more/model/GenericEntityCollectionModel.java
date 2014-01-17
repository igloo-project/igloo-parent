package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class GenericEntityCollectionModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, Collection<E>> {

	private static final long serialVersionUID = -2960630322941056821L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> GenericEntityCollectionModel<K, E> of(
			Class<E> clazz) {
		return new GenericEntityCollectionModel<K, E>(clazz, Suppliers2.<E>arrayList());
	}

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> GenericEntityCollectionModel<K, E> of(
			Class<E> clazz, Supplier<? extends Collection<E>> collectionSupplier) {
		return new GenericEntityCollectionModel<K, E>(clazz, collectionSupplier);
	}

	protected GenericEntityCollectionModel(Class<E> clazz, Supplier<? extends Collection<E>> collectionSupplier) {
		super(clazz, collectionSupplier);
	}

}
