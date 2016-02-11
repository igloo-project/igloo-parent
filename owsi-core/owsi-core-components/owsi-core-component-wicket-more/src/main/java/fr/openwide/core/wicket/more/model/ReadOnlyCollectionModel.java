package fr.openwide.core.wicket.more.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.primitives.Ints;

import fr.openwide.core.commons.util.collections.Iterators2;
import fr.openwide.core.wicket.more.markup.repeater.collection.ICollectionModel;

public class ReadOnlyCollectionModel<T, C extends Collection<T>>
		implements ICollectionModel<T, C>, IComponentAssignedModel<C> {
	
	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends C> readModel;
	
	private final Function<? super T, ? extends IModel<T>> itemModelFactory;
	
	public static <T, C extends Collection<T>> ReadOnlyCollectionModel<T, C> of(
			IModel<? extends C> model, Function<? super T, ? extends IModel<T>> keyFactory) {
		return new ReadOnlyCollectionModel<T, C>(model, keyFactory);
	}

	protected ReadOnlyCollectionModel(IModel<? extends C> readModel, Function<? super T, ? extends IModel<T>> keyFactory) {
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
	
	@Override
	public Iterator<IModel<T>> iterator(long offset, long limit) {
		Iterable<IModel<T>> offsetedIterable = Iterables.skip(
				internalIterable(), Ints.saturatedCast(offset)
		);
		return Iterators.limit(iterator(offsetedIterable), Ints.saturatedCast(limit));
	}

	private Iterable<IModel<T>> internalIterable() {
		C collection = readModel.getObject();
		if (collection == null) {
			return ImmutableSet.<IModel<T>>of();
		} else {
			return Iterables.transform(collection, itemModelFactory);
		}
	}
	
	private Iterator<IModel<T>> iterator(Iterable<IModel<T>> iterable) {
		/* RefreshingView gets this iterator and *then* detaches its items, which may indirectly detach
		 * this model and hence make changes to the modelMap.
		 * That's why we must use a deferred iterator here. 
		 */
		Iterator<IModel<T>> iterator = Iterators2.deferred(iterable);
		return Iterators.unmodifiableIterator(iterator);
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
			super(wrap(readModel, component), itemModelFactory);
		}
		
		@Override
		public IModel<?> getWrappedModel() {
			return ReadOnlyCollectionModel.this;
		}
	}
	
	private static <T> IModel<? extends T> wrap(IModel<? extends T> model, Component component) {
		if (model instanceof IComponentAssignedModel) {
			return ((IComponentAssignedModel<? extends T>)model).wrapOnAssignment(component);
		} else {
			return model;
		}
	}

}
