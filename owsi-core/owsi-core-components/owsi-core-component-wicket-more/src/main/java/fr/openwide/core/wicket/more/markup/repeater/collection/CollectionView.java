package fr.openwide.core.wicket.more.markup.repeater.collection;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class CollectionView<T> extends AbstractPageableView<T> {

	private static final long serialVersionUID = 1L;
	
	private final ICollectionModel<T, ?> collectionModel;
	
	public CollectionView(String id, ICollectionModel<T, ?> collectionModel) {
		super(id, collectionModel);
		this.collectionModel = collectionModel;
	}
	
	@Override
	protected Iterator<IModel<T>> getItemModels(long offset, long size) {
		return new ExactTypeIterator<IModel<T>>(collectionModel.iterator(offset, size));
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
		return collectionModel.count();
	}
	
	@Override
	protected abstract void populateItem(Item<T> item);

	public ICollectionModel<T, ?> getModel() {
		return collectionModel;
	}

}
