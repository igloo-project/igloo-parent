package org.iglooproject.wicket.more.markup.repeater.map;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.model.IItemModelAwareMapModel;

/**
 * A collection view whose items provide access to their model through a more precise form than just
 * {@code IModel<T>}.
 * 
 * <p>Useful when using models with additional capabilities (i.e. additional methods).
 * 
 * @author yrodiere
 *
 * @param <K> The key type
 * @param <V> The value type
 * @param <MK> The key model type
 * @param <MV> The value model type
 */
public abstract class SpecificModelMapView<K, V, MK extends IModel<K>, MV extends IModel<V>>
		extends MapView<K, V> {
	
	private static final long serialVersionUID = 1L;

	public SpecificModelMapView(String id, IItemModelAwareMapModel<K, V, ?, ? extends MK, ? extends MV> collectionModel) {
		super(id, collectionModel);
	}

	@Override
	protected final void populateItem(MapItem<K, V> item) {
		populateItem((SpecificModelItem)item);
	}

	protected abstract void populateItem(SpecificModelItem item);
	
	@Override
	@SuppressWarnings("unchecked") // The model is known to be a IItemModelAwareCollectionModel, see constructor
	public IItemModelAwareMapModel<K, V, ?, ? extends MK, ? extends MV> getModel() {
		return (IItemModelAwareMapModel<K, V, ?, ? extends MK, ? extends MV>)super.getModel();
	}

	@Override
	@SuppressWarnings("unchecked") // The type of "model" is enforced by the mapModel
	protected SpecificModelItem newItem(String id, int index, IModel<K> model) {
		return new SpecificModelItem(id, index, (MK) model, getModel().valueModelForProvidedKeyModel(model));
	}
	
	public class SpecificModelItem extends MapItem<K, V> {
		private static final long serialVersionUID = 1L;
		
		public SpecificModelItem(String id, int index, MK model, MV valueModel) {
			super(id, index, model, valueModel);
		}
		
		@SuppressWarnings("unchecked")
		public MK getSpecificModel() {
			return (MK)getModel();
		}
		
		@SuppressWarnings("unchecked")
		public MV getSpecificValueModel() {
			return (MV)getValueModel();
		}
	}
}
