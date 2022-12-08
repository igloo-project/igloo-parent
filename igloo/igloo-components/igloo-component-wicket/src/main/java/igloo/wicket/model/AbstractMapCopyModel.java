package igloo.wicket.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * An abstract base for implementations of {@link IMapModel} whose content is to be "cloned" (i.e. copied to a new
 * map) each time {@link #setObject(Map)} is called.
 * 
 * <p>This is typically what you want when editing a map in a form.
 * 
 * <p>Content is stored as a map of element models. Thus, subclasses of this one are guaranteed to always return the
 * same model for a given element (key or value), up to this element's removal from the map.
 * 
 * <p><strong>WARNING:</strong> this model is only intended to contain small maps. It is absolutely not optimized
 * for large maps (say, more than just one or two dozens of items). Performance issues may arise when dealing
 * with large maps.
 */
abstract class AbstractMapCopyModel<K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
		extends LoadableDetachableModel<M>
		implements IItemModelAwareMapModel<K, V, M, MK, MV> {

	private static final long serialVersionUID = 8313241207877097043L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMapCopyModel.class);
	
	private final Map<MK, MV> modelMap = Maps.newLinkedHashMap();
	
	public AbstractMapCopyModel() {
		super();
	}
	
	@Override
	public void detach() {
		if (!isAttached()) {
			/*
			 * Make sure the models are given a chance to process post-detach changes on their object.
			 * Useful in particular for GenericEntityModel.detach()
			 */
			Detachables.detach(modelMap);
			return;
		} else {
			super.detach();
		}
	}

	@Override
	protected void onDetach() {
		updateModels();
		Detachables.detach(modelMap);
	}
	
	protected final void updateModelsIfExternalChangeIsPossible() {
		if (isAttached()) {
			updateModels();
		}
	}

	private void updateModels() {
		// Save the previous models for re-use based on the key model's current content
		Map<K, Pair<MK, MV>> oldModels = Maps.newHashMap();
		for (Map.Entry<MK, MV> entry : modelMap.entrySet()) {
			K key = entry.getKey().getObject();
			Object previousModelsForThisKey = oldModels.put(key, Pair.with(entry.getKey(), entry.getValue()));
			if (previousModelsForThisKey != null) {
				LOGGER.warn(
						"Detected multiple models for the key {} in {}. One key/value pair might have been lost while"
						+ " updating models."
						+ " You probably get this warning because you called setObject() on a key model, which is"
						+ " not recommended.",
						key, modelMap
				);
			}
		}
		
		// Build the model map based on the current object
		modelMap.clear();
		Map<K, V> currentObject = getObject();
		for (Entry<K, V> item : currentObject.entrySet()) {
			Pair<MK, MV> existingPair = oldModels.get(item.getKey());
			MK keyModel;
			MV valueModel;
			if (existingPair != null) {
				// Re-use existing models
				keyModel = existingPair.getValue0();
				valueModel = existingPair.getValue1();
				
				V oldValue = valueModel.getObject();
				V newValue = item.getValue();
				
				if (!Objects.equals(oldValue, newValue)) {
					valueModel.setObject(newValue);
				}
			} else {
				// Otherwise, create new models
				keyModel = createKeyModel(item.getKey());
				valueModel = createValueModel(item.getValue());
			}
			modelMap.put(keyModel, valueModel);
		}
	}

	/**
	 * WARNING: if the client calls <code>setObject(null)</code>, a subsequent call to <code>getObject()</code>
	 * will not return <code>null</code>, but <em>an empty map</em>.
	 */
	@Override
	public final void setObject(M object) {
		M map = createMap();
		if (object != null) {
			map.putAll(object);
		}
		super.setObject(map);
	}

	@Override
	protected final M load() {
		M map = createMap();
		
		for (Entry<MK, MV> item : modelMap.entrySet()) {
			map.put(item.getKey().getObject(), item.getValue().getObject());
		}
		
		return map;
	}
	
	@Override
	public Iterator<MK> iterator() {
		return keysModel().iterator();
	}
	
	@Override
	public Iterator<MK> iterator(long offset, long limit) {
		return keysModel().iterator(offset, limit);
	}

	@Override
	public IItemModelAwareCollectionModel<K, Set<K>, MK> keysModel() {
		return new KeysModel();
	}
	
	protected class KeysModel extends AbstractMapCollectionModel<K, Set<K>, MK> {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			AbstractMapCopyModel.this.detach();
		}
		@Override
		public Set<K> getObject() {
			Map<K, ?> map = AbstractMapCopyModel.this.getObject();
			return map == null ? null : map.keySet();
		}

		@Override
		protected Iterable<MK> internalIterable() {
			updateModelsIfExternalChangeIsPossible();
			return modelMap.keySet();
		}
	}
	
	@Override
	public IItemModelAwareCollectionModel<V, Collection<V>, MV> valuesModel() {
		return new ValuesModel();
	}
	
	protected class ValuesModel extends AbstractMapCollectionModel<V, Collection<V>, MV> {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			AbstractMapCopyModel.this.detach();
		}
		@Override
		public Collection<V> getObject() {
			Map<?, V> map = AbstractMapCopyModel.this.getObject();
			return map == null ? null : map.values();
		}

		@Override
		protected Iterable<MV> internalIterable() {
			updateModelsIfExternalChangeIsPossible();
			return modelMap.values();
		}
	}
	
	@Override
	public final long size() {
		updateModelsIfExternalChangeIsPossible();
		return modelMap.size();
	}

	@Override
	public final IModel<V> valueModel(final IModel<? extends K> keyModel) {
		return Models.mapModelValueModel(this, keyModel);
	}

	@Override
	public final MV valueModelForProvidedKeyModel(final IModel<K> keyModel) {
		// We don't need to update the modelMap here, as the keyModel is supposed to have been provided by this object
		return modelMap.get(keyModel);
	}

	@Override
	public final void put(K key, V value) {
		/*
		 * We must put to an instance of the underlying map, not to the model map, so as to respect the
		 * properties of this map (make the map itself pick the element to be replaced, for instance).
		 */
		M map = getObject();
		map.put(key, value);
		updateModelsIfExternalChangeIsPossible();
	}

	@Override
	public final void remove(K key) {
		/*
		 * We must remove from an instance of the underlying map, not from the model map, so as to respect the
		 * properties of this map (make the map itself pick the element to be removed).
		 */
		M map = getObject();
		map.remove(key);
		updateModelsIfExternalChangeIsPossible();
	}
	
	@Override
	public final void clear() {
		super.setObject(createMap()); // Remove the cached collection from LoadableDetachableModel
		modelMap.clear();
		detach(); 
	}

	protected abstract M createMap();

	protected abstract MK createKeyModel(K key);

	protected abstract MV createValueModel(V value);

}
