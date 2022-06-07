package org.iglooproject.wicket.more.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.model.IItemModelAwareCollectionModel;
import org.iglooproject.wicket.model.Models;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

public class ReadOnlyCollectionModel<T, C extends Collection<T>>
		implements IItemModelAwareCollectionModel<T, C, IModel<T>>, IComponentAssignedModel<C> {
	
	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends C> readModel;
	
	private final SerializableFunction2<? super T, ? extends IModel<T>> itemModelFactory;
	
	public static <T, C extends Collection<T>> ReadOnlyCollectionModel<T, C> of(
			IModel<? extends C> model, SerializableFunction2<? super T, ? extends IModel<T>> keyFactory) {
		return new ReadOnlyCollectionModel<>(model, keyFactory);
	}

	protected ReadOnlyCollectionModel(IModel<? extends C> readModel, SerializableFunction2<? super T, ? extends IModel<T>> keyFactory) {
		super();
		this.readModel = checkNotNull(readModel);
		this.itemModelFactory = checkNotNull(keyFactory);
	}

	@Override
	public C getObject() {
		return readModel.getObject();
	}
	
	@Override
	public void setObject(C object) {
		throw newReadOnlyException();
	}
	
	private UnsupportedOperationException newReadOnlyException() {
		return new UnsupportedOperationException("Model " + getClass() + " is read-only");
	}

	@Override
	public void detach() {
		readModel.detach();
	}
	
	private Collection<T> internalCollection() {
		C collection = readModel.getObject();
		if (collection == null) {
			return ImmutableSet.<T>of();
		} else {
			return collection;
		}
	}
	
	@Override
	public Iterator<IModel<T>> iterator() {
		return Streams.stream(Models.collectionModelIterator(internalCollection()))
				.map(itemModelFactory)
				.collect(Collectors.<IModel<T>>toList())
				.iterator();
	}

	@Override
	public final Iterator<IModel<T>> iterator(long offset, long limit) {
		// Transform must be the last operation, in order to let collectionModelIterator() optimize stuff
		return Streams.stream(Models.collectionModelIterator(internalCollection(), offset, limit))
				.map(itemModelFactory)
				.collect(Collectors.<IModel<T>>toList())
				.iterator();
	}
	
	@Override
	public long size() {
		C collection = readModel.getObject();
		if (collection == null) {
			return 0L;
		} else {
			return collection.size();
		}
	}

	@Override
	public void add(T value) {
		throw newReadOnlyException();
	}

	@Override
	public void remove(T value) {
		throw newReadOnlyException();
	}

	@Override
	public void clear() {
		throw newReadOnlyException();
	}

	@Override
	public IWrapModel<C> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}
	
	private class WrapModel extends ReadOnlyCollectionModel<T, C> implements IWrapModel<C> {
		private static final long serialVersionUID = 7996314523359141428L;
		
		protected WrapModel(Component component) {
			super(Models.wrap(readModel, component), itemModelFactory);
		}
		
		@Override
		public IModel<?> getWrappedModel() {
			return ReadOnlyCollectionModel.this;
		}
	}
	
}
