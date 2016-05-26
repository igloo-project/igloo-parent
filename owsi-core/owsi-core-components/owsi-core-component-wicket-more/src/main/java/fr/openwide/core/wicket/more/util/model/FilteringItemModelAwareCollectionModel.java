package fr.openwide.core.wicket.more.util.model;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fr.openwide.core.wicket.more.markup.repeater.collection.IItemModelAwareCollectionModel;

class FilteringItemModelAwareCollectionModel<T, C extends Collection<T>, M extends IModel<T>>
		implements IItemModelAwareCollectionModel<T, C, M> {
	
	private static final long serialVersionUID = 3675361542494678205L;

	private final IItemModelAwareCollectionModel<T, C, M> unfiltered;
	
	private final Predicate<M> predicate;
	
	public FilteringItemModelAwareCollectionModel(IItemModelAwareCollectionModel<T, C, M> delegate, Predicate<M> predicate) {
		super();
		this.unfiltered = delegate;
		this.predicate = predicate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FilteringItemModelAwareCollectionModel)) {
			return false;
		}
		FilteringItemModelAwareCollectionModel<?, ?, ?> other = (FilteringItemModelAwareCollectionModel<?, ?, ?>) obj;
		return new EqualsBuilder()
				.append(predicate, other.predicate)
				.append(unfiltered, other.unfiltered)
				.build();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(predicate)
				.append(unfiltered)
				.build();
	}

	@Override
	public void add(T item) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(T item) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public C getObject() {
		return unfiltered.getObject();
	}

	@Override
	public void setObject(C object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach() {
		unfiltered.detach();
	}

	@Override
	public long size() {
		return unfiltered.size();
	}

	private Iterable<M> getFilteredIterable() {
		return Iterables.filter(unfiltered, predicate);
	}

	@Override
	public Iterator<M> iterator() {
		return Models.collectionModelIterator(getFilteredIterable());
	}

	@Override
	public final Iterator<M> iterator(long offset, long limit) {
		return Models.collectionModelIterator(getFilteredIterable(), offset, limit);
	}

}
