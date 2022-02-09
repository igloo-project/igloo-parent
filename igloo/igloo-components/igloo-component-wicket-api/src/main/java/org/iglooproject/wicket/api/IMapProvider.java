package org.iglooproject.wicket.api;

import java.util.NoSuchElementException;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.repeater.ISequenceProvider;

public interface IMapProvider<K, V> extends ISequenceProvider<K> {

	/**
	 * @param keyModel A model that (on contrary to {@link #valueModelForProvidedKeyModel(IModel)}) may not have been
	 * {@link #iterator(long, long) provided} by this {@link IMapProvider} or may have been wrapped.
	 * @return A wrapper model that will dynamically return the value associated to the key model's content when
	 * {@link IModel#getObject()} is called, and set it when {@link IModel#setObject(Object)} is called.
	 * On contrary to {@link #valueModelForProvidedKeyModel(IModel)}, the behavior of this method is unchanged
	 * if the given keyModel has not been obtained through this {@link IMapProvider} or if it has been wrapped.
	 */
	IModel<V> valueModel(IModel<? extends K> keyModel);

	/**
	 * @param keyModel A model <strong>that has been {@link #iterator(long, long) provided} by this {@link IMapProvider}
	 * </strong> and <strong>has not been wrapped</strong>
	 * @return The value model matching the given key model. Behavior is undefined if the given keyModel has not been
	 * obtained through this {@link IMapProvider} or if it has been wrapped.
	 * @throws NoSuchElementException If the given key model is not part of this map provider.
	 */
	IModel<V> valueModelForProvidedKeyModel(IModel<K> keyModel);

}
