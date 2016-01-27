package fr.openwide.core.wicket.more.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Map;

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
import fr.openwide.core.wicket.more.markup.repeater.map.IMapModel;

public class ReadOnlyMapModel<K, V, M extends Map<K, V>> extends AbstractMapModel<K, V, M>
		implements IMapModel<K, V, M>, IComponentAssignedModel<M> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends M> readModel;
	
	private final Function<? super K, ? extends IModel<K>> keyModelFactory;
	
	public static <K, V, M extends Map<K, V>> ReadOnlyMapModel<K, V, M> of(
			IModel<? extends M> model, Function<? super K, ? extends IModel<K>> keyFactory) {
		return new ReadOnlyMapModel<K, V, M>(model, keyFactory);
	}

	protected ReadOnlyMapModel(IModel<? extends M> readModel, Function<? super K, ? extends IModel<K>> keyFactory) {
		super();
		this.readModel = checkNotNull(readModel);
		this.keyModelFactory = checkNotNull(keyFactory);
	}

	@Override
	public M getObject() {
		return readModel.getObject();
	}
	
	@Override
	public void setObject(M object) {
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
	public IModel<V> getValueModelForProvidedKeyModel(IModel<K> keyModel) {
		return getValueModel(keyModel);
	}
	
	@Override
	public Iterator<IModel<K>> iterator(long offset, long limit) {
		Iterable<IModel<K>> offsetedIterable = Iterables.skip(
				internalIterable(), Ints.saturatedCast(offset)
		);
		return Iterators.limit(iterator(offsetedIterable), Ints.saturatedCast(limit));
	}

	private Iterable<IModel<K>> internalIterable() {
		M map = readModel.getObject();
		if (map == null) {
			return ImmutableSet.<IModel<K>>of();
		} else {
			return Iterables.transform(map.keySet(), keyModelFactory);
		}
	}
	
	private Iterator<IModel<K>> iterator(Iterable<IModel<K>> iterable) {
		/* RefreshingView gets this iterator and *then* detaches its items, which may indirectly detach
		 * this model and hence make changes to the modelMap.
		 * That's why we must use a deferred iterator here. 
		 */
		Iterator<IModel<K>> iterator = Iterators2.deferred(iterable);
		return Iterators.unmodifiableIterator(iterator);
	}

	@Override
	public long count() {
		M map = readModel.getObject();
		if (map == null) {
			return 0L;
		} else {
			return map.size();
		}
	}

	@Override
	public void put(K key, V value) {
		throw newReadOnlyException();
	}

	@Override
	public void remove(K key) {
		throw newReadOnlyException();
	}

	@Override
	public void clear() {
		throw newReadOnlyException();
	}

	@Override
	public IWrapModel<M> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}
	
	private class WrapModel extends ReadOnlyMapModel<K, V, M> implements IWrapModel<M> {
		private static final long serialVersionUID = 7996314523359141428L;
		
		protected WrapModel(Component component) {
			super(wrap(readModel, component), keyModelFactory);
		}
		
		@Override
		public IModel<?> getWrappedModel() {
			return ReadOnlyMapModel.this;
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
