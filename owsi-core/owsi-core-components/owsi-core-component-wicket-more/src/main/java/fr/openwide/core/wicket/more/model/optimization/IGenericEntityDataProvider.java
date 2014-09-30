package fr.openwide.core.wicket.more.model.optimization;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.model.IBindableDataProvider;

public interface IGenericEntityDataProvider<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends IDataProvider<E>, IBindableDataProvider {
	
	Iterator<? extends K> idIterator(long first, long count);

}
