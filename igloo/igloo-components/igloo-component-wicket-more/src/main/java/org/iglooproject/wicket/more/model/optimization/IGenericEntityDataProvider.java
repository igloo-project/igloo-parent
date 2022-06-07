package org.iglooproject.wicket.more.model.optimization;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.model.IBindableDataProvider;

public interface IGenericEntityDataProvider<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends IDataProvider<E>, IBindableDataProvider {
	
	Iterator<? extends K> idIterator(long first, long count);

}
