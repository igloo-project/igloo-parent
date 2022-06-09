package igloo.wicket.model;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.model.IModel;

import com.google.common.collect.Iterables;

abstract class AbstractMapCollectionModel<T, C extends Collection<T>, M extends IModel<T>>
		implements IItemModelAwareCollectionModel<T, C, M> {

	private static final long serialVersionUID = 1L;

	@Override
	public abstract void detach();

	@Override
	public abstract C getObject();

	@Override
	public void setObject(C object) {
		throw newReadOnlyException();
	}

	@Override
	public long size() {
		return Iterables.size(internalIterable());
	}

	protected abstract Iterable<M> internalIterable();

	@Override
	public Iterator<M> iterator() {
		return Models.collectionModelIterator(internalIterable());
	}

	@Override
	public Iterator<M> iterator(long offset, long limit) {
		return Models.collectionModelIterator(internalIterable(), offset, limit);
	}

	@Override
	public void add(T item) {
		throw newReadOnlyException();
	}

	@Override
	public void remove(T item) {
		throw newReadOnlyException();
	}

	@Override
	public void clear() {
		throw newReadOnlyException();
	}

	protected UnsupportedOperationException newReadOnlyException() {
		return new UnsupportedOperationException("Model " + getClass() + " is read-only");
	}

}
