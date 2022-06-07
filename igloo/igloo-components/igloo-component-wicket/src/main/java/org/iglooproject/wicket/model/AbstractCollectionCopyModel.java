package org.iglooproject.wicket.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * An abstract base for implementations of {@link ICollectionModel} whose content is to be "cloned" (i.e. copied to a new
 * collection) each time {@link #setObject(Collection)} is called.
 * 
 * <p>This is typically what you want when editing a collection in a form.
 * 
 * <p>Content is stored as a list of element models. Thus, subclasses of this one are guaranteed to always return the
 * same model for a given element, up to this element's removal from the collection.
 * 
 * <p><strong>WARNING:</strong> this model is only intended to contain small collections. It is absolutely not optimized
 * for large collections (say, more than just one or two dozens of items). Performance issues may arise when dealing
 * with large collections.
 */
abstract class AbstractCollectionCopyModel<T, C extends Collection<T>, M extends IModel<T>>
		extends LoadableDetachableModel<C> 
		implements IItemModelAwareCollectionModel<T, C, M> {

	private static final long serialVersionUID = -1768835911782930879L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCollectionCopyModel.class);
	
	private final List<M> modelList = Lists.newArrayList();
	
	public AbstractCollectionCopyModel() {
		super();
	}
	
	@Override
	public void detach() {
		if (!isAttached()) {
			/*
			 * Make sure the models are given a chance to process post-detach changes on their object.
			 * Useful in particular for GenericEntityModel.detach()
			 */
			Detachables.detach(modelList);
			return;
		} else {
			super.detach();
		}
	}
	
	@Override
	protected void onDetach() {
		updateModelsIfExternalChangeIsPossible();
		Detachables.detach(modelList);
	}
	
	protected final void updateModelsIfExternalChangeIsPossible() {
		if (isAttached()) {
			updateModels();
		}
	}
	
	private void updateModels() {
		// Save the previous models for re-use based on their current content
		Map<T, M> oldModels = Maps.newHashMap();
		int index = 0;
		for (M model : modelList) {
			T value = model.getObject();
			Object previousModelForThisValue = oldModels.put(model.getObject(), model);
			if (previousModelForThisValue != null) {
				LOGGER.warn(
						"Detected multiple models for the same value {} at index {} in {}. One value might have been"
						+ " lost while updating models."
						+ " If you need a model supporting multiple identical items, use an index-based implementation",
						value, index, model
				);
			}
			++index;
		}

		// Build the model list based on the current object
		modelList.clear();
		C currentObject = getObject();
		for (T item : currentObject) {
			M itemModel = oldModels.get(item);
			if (itemModel == null) {
				// No existing model, create a new one
				itemModel = createModel(item);
			}
			modelList.add(itemModel);
		}
	}

	/**
	 * WARNING: if the client calls <code>setObject(null)</code>, a subsequent call to <code>getObject()</code>
	 * will not return <code>null</code>, but <em>an empty collection</em>.
	 */
	@Override
	public void setObject(C object) {
		C collection = createCollection();
		if (object != null) {
			collection.addAll(object);
		}
		super.setObject(collection);
	}
	
	@Override
	protected final C load() {
		C collection = createCollection();
		
		for (IModel<? extends T> itemModel : modelList) {
			collection.add(itemModel.getObject());
		}
		
		return collection;
	}
	
	@Override
	public Iterator<M> iterator() {
		updateModelsIfExternalChangeIsPossible();
		return Models.collectionModelIterator(modelList);
	}

	@Override
	public final Iterator<M> iterator(long offset, long limit) {
		updateModelsIfExternalChangeIsPossible();
		return Models.collectionModelIterator(modelList, offset, limit);
	}
	
	@Override
	public final long size() {
		updateModelsIfExternalChangeIsPossible();
		return modelList.size();
	}
	
	@Override
	public final void add(T item) {
		/*
		 * We must add to an instance of the underlying collection, not to the model list, so as to respect the
		 * properties of this collection (make the collection itself pick the element to be replaced, for instance).
		 */
		C collection = getObject();
		collection.add(item);
		updateModelsIfExternalChangeIsPossible();
	}
	
	@Override
	public final void remove(T item) {
		/*
		 * We must remove from an instance of the underlying collection, not from the model list, so as to respect the
		 * properties of this collection (make the collection itself pick the element to be removed).
		 */
		C collection = getObject();
		collection.remove(item);
		updateModelsIfExternalChangeIsPossible();
	}
	
	@Override
	public void clear() {
		super.setObject(createCollection()); // Remove the cached collection from LoadableDetachableModel
		modelList.clear();
		detach(); 
	}

	protected abstract C createCollection();

	protected abstract M createModel(T item);

}
