package igloo.wicket.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.functional.SerializableSupplier2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

class FilterByModelItemModelAwareCollectionModel<T, C extends Collection<T>, M extends IModel<T>>
		implements IItemModelAwareCollectionModel<T, C, M> {
	
	private static final long serialVersionUID = 3675361542494678205L;

	private final IItemModelAwareCollectionModel<T, ? extends Collection<T>, M> unfiltered;
	
	private final SerializablePredicate2<M> modelPredicate;
	
	private final SerializableSupplier2<? extends C> collectionSupplier;
	
	public FilterByModelItemModelAwareCollectionModel(
			IItemModelAwareCollectionModel<T, ? extends Collection<T>, M> delegate,
			SerializablePredicate2<M> modelPredicate,
			SerializableSupplier2<? extends C> collectionSupplier
	) {
		super();
		this.unfiltered = delegate;
		this.modelPredicate = modelPredicate;
		this.collectionSupplier = collectionSupplier;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().equals(getClass())) {
			return false;
		}
		FilterByModelItemModelAwareCollectionModel<?, ?, ?> other = (FilterByModelItemModelAwareCollectionModel<?, ?, ?>) obj;
		return new EqualsBuilder()
				.append(modelPredicate, other.modelPredicate)
				.append(unfiltered, other.unfiltered)
				.build();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(modelPredicate)
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
		C result = collectionSupplier.get();
		for (M model : this) {
			result.add(model.getObject());
		}
		return result;
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
		return Iterables.size(getFilteredIterable());
	}

	private Iterable<M> getFilteredIterable() {
		Objects.requireNonNull(unfiltered);
		return ImmutableList.copyOf(unfiltered).stream().filter(modelPredicate)::iterator;
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
