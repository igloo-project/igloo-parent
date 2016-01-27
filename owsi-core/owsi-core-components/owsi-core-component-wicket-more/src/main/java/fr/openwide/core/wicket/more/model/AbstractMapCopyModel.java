package fr.openwide.core.wicket.more.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

import fr.openwide.core.commons.util.collections.Iterators2;
import fr.openwide.core.wicket.more.markup.repeater.map.IItemModelAwareMapModel;
import fr.openwide.core.wicket.more.markup.repeater.map.IMapModel;
import fr.openwide.core.wicket.more.util.model.Detachables;

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
			/**
			 * Make sure the models are given a change to process post-detach changes on their object.
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
				valueModel.setObject(item.getValue());
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
	public final Iterator<MK> iterator(long offset, long limit) {
		updateModelsIfExternalChangeIsPossible();
		Iterable<MK> offsetedIterable = Iterables.skip(
				modelMap.keySet(), Ints.saturatedCast(offset)
		);
		return Iterators.limit(iterator(offsetedIterable), Ints.saturatedCast(limit));
	}
	
	private Iterator<MK> iterator(Iterable<MK> iterable) {
		/* RefreshingView gets this iterator and *then* detaches its items, which may indirectly detach
		 * this model and hence make changes to the modelMap.
		 * That's why we must use a deferred iterator here. 
		 */
		Iterator<MK> iterator = Iterators2.deferred(iterable);
		return Iterators.unmodifiableIterator(iterator);
	}
	
	@Override
	public final long count() {
		updateModelsIfExternalChangeIsPossible();
		return modelMap.size();
	}
	
	@Override
	public final Iterator<MK> iterator() {
		updateModelsIfExternalChangeIsPossible();
		return iterator(modelMap.keySet());
	}

	@Override
	public final IModel<V> getValueModel(final IModel<? extends K> keyModel) {
		return new IModel<V>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void detach() {
				AbstractMapCopyModel.this.detach();
				keyModel.detach();
			}
			@Override
			public V getObject() {
				return AbstractMapCopyModel.this.getObject().get(keyModel.getObject());
			}
			@Override
			public void setObject(V object) {
				AbstractMapCopyModel.this.put(keyModel.getObject(), object);
			}
		};
	}

	@Override
	public final MV getValueModelForProvidedKeyModel(final IModel<K> keyModel) {
		updateModelsIfExternalChangeIsPossible();
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
	}

	@Override
	public final void remove(K key) {
		/*
		 * We must remove from an instance of the underlying map, not from the model map, so as to respect the
		 * properties of this map (make the map itself pick the element to be removed).
		 */
		M map = getObject();
		map.remove(key);
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
