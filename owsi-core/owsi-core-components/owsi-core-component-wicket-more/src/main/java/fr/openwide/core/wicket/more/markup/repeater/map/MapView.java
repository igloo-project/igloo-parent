package fr.openwide.core.wicket.more.markup.repeater.map;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class MapView<K, V> extends AbstractPageableView<K> {

	private static final long serialVersionUID = 1L;
	
	private final IMapModel<K, V, ?> mapModel;
	
	public MapView(String id, IMapModel<K, V, ?> mapModel) {
		super(id);
		this.mapModel = mapModel;
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		mapModel.detach();
	}
	
	@Override
	protected Iterator<IModel<K>> getItemModels(long offset, long size) {
		return new ExactTypeIterator<IModel<K>>(mapModel.iterator(offset, size));
	}
	
	private static class ExactTypeIterator<T> implements Iterator<T>  {
		private final Iterator<? extends T> delegate;
		
		public ExactTypeIterator(Iterator<? extends T> delegate) {
			super();
			this.delegate = delegate;
		}

		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		@Override
		public T next() {
			return delegate.next();
		}

		@Override
		public void remove() {
			delegate.remove();
		}

	}
	
	@Override
	protected long internalGetItemCount() {
		return mapModel.count();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected final void populateItem(Item<K> item) {
		populateItem((MapItem<K, V>)item);
	}

	protected abstract void populateItem(MapItem<K, V> item);

	public IMapModel<K, V, ?> getModel() {
		return mapModel;
	}

	@Override
	protected MapItem<K, V> newItem(String id, int index, IModel<K> model) {
		return new MapItem<K, V>(id, index, model, mapModel.getValueModelForProvidedKeyModel(model));
	}

}
