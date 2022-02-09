package org.iglooproject.wicket.more.markup.repeater.collection;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.IItemModelAwareCollectionModel;

/**
 * A collection view whose items provide access to their model through a more precise form than just
 * {@code IModel<T>}.
 * 
 * <p>Useful when using models with additional capabilities (i.e. additional methods).
 * 
 * @author yrodiere
 *
 * @param <T> The item type
 * @param <M> The item model type
 */
public abstract class SpecificModelCollectionView<T, M extends IModel<T>> extends CollectionView<T> {
	
	private static final long serialVersionUID = 1L;

	public SpecificModelCollectionView(String id, IItemModelAwareCollectionModel<T, ?, ? extends M> collectionModel) {
		super(id, collectionModel);
	}

	@Override
	protected final void populateItem(Item<T> item) {
		populateItem((SpecificModelItem)item);
	}

	protected abstract void populateItem(SpecificModelItem item);
	
	@Override
	@SuppressWarnings("unchecked") // The model is known to be a IItemModelAwareCollectionModel, see constructor
	public IItemModelAwareCollectionModel<T, ?, ? extends M> getModel() {
		return (IItemModelAwareCollectionModel<T, ?, ? extends M>)super.getModel();
	}

	@Override
	@SuppressWarnings("unchecked") // The type of "model" is enforced by the collectionModel
	protected SpecificModelItem newItem(String id, int index, IModel<T> model) {
		return new SpecificModelItem(id, index, (M) model);
	}
	
	public class SpecificModelItem extends Item<T> {
		private static final long serialVersionUID = 1L;
		
		public SpecificModelItem(String id, int index, M model) {
			super(id, index, model);
		}
		
		@SuppressWarnings("unchecked")
		public M getSpecificModel() {
			return (M)getModel();
		}
	}
}
