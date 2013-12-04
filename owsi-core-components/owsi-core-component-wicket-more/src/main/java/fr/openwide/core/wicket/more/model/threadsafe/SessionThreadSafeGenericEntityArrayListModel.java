package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class SessionThreadSafeGenericEntityArrayListModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractSessionThreadSafeGenericEntityCollectionModel<K, E, List<E>> {
	
	private static final long serialVersionUID = -3618904709308977111L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			SessionThreadSafeGenericEntityArrayListModel<K, E> of(Class<E> clazz) {
		return new SessionThreadSafeGenericEntityArrayListModel<K, E>(clazz);
	}

	public SessionThreadSafeGenericEntityArrayListModel(Class<E> clazz) {
		super(clazz);
	}

	@Override
	protected List<E> createEntityCollection() {
		return Lists.newArrayList();
	}

}
