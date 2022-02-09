package org.iglooproject.wicket.more.bindable.model;

import java.util.Collection;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.api.ICollectionModel;
import org.iglooproject.wicket.api.IMapModel;
import org.iglooproject.wicket.more.bindable.exception.NoSuchModelException;

/**
 * An {@link IModel} that also provides centralized access to its properties (referenced using
 * {@link BindingRoot Bindings}), which allows it to provide value caching capabilities.
 */
public interface IBindableModel<T> extends IModel<T> {
	
	/**
	 * Returns a model bound to <code>binding</code>, with an internal cache, using the <code>workingCopyProposal</code>.
	 * <p>If {@link #bindWithCache(BindingRoot, IModel)} has already been called, the exact same model will be returned.
	 * <p>When {@link #readAll()} is called on this {@link IBindableModel}, {@link #readAll()} will automatically be called
	 * on the returned model.
	 * <p>When {@link #readAllUnder(BindingRoot)} is called on this {@link IBindableModel}, {@link #readAllUnder(BindingRoot)} will
	 * automatically be called on the returned model.
	 * <p>When {@link #writeAll()} is called on this {@link IBindableModel}, {@link #writeAll()} will automatically
	 * be called on the returned model.
	 */
	<T2> IBindableModel<T2> bind(BindingRoot<? super T, T2> binding);
	
	/**
	 * Returns a model bound to <code>binding</code>, with an internal cache, using the <code>workingCopyProposal</code>.
	 * <p>If {@link #bindWithCache(BindingRoot, IModel)} or {@link #bind(BindingRoot)} has already been called for
	 * the same property, the exact same model will be returned.
	 * <p>When {@link #readAll()} is called on this {@link IBindableModel}, {@link #readAll()} will automatically be called
	 * on the returned model.
	 * <p>When {@link #readAllUnder(BindingRoot)} is called on this {@link IBindableModel}, {@link #readAllUnder(BindingRoot)} will
	 * automatically be called on the returned model.
	 * <p>When {@link #writeAll()} is called on this {@link IBindableModel}, {@link #writeAll()} will automatically
	 * be called on the returned model.
	 */
	<T2> IBindableModel<T2> bindWithCache(BindingRoot<? super T, T2> binding, IModel<T2> workingCopyProposal);

	/**
	 * Returns a {@link ICollectionModel} which caches the content of the collection, and whose items models are
	 * instances of a specific type of {@link IModel}, chosen by the user, which allows the user to define how
	 * to serialize items.
	 * 
	 * <p>Optionally, item models may be instances of {@link IBindableModel}, which allows the
	 * user to define what to cache on these models.
	 */
	<T2, C extends Collection<T2>> IBindableCollectionModel<T2, C> bindCollectionWithCache(
			BindingRoot<? super T, C> binding,
			SerializableSupplier2<? extends C> newCollectionSupplier,
			SerializableFunction2<? super T2, ? extends IModel<T2>> itemModelFunction
	);
	
	/**
	 * @return A previously created model collection tied to the given binding.
	 * @throws NoSuchModelException If no collection model was added for the given binding.
	 */
	<T2, C extends Collection<T2>> IBindableCollectionModel<T2, C> bindCollectionAlreadyAdded(BindingRoot<? super T, C> binding);

	/**
	 * Returns a {@link IMapModel} which caches the content of the map, and whose items models are
	 * instances of a specific type of {@link IModel}, chosen by the user, which allows the user to define how
	 * to serialize keys and values.
	 * 
	 * <p>Optionally, key and/or value models may be instances of {@link IBindableModel}, which allows the
	 * user to define what to cache on these models.
	 */
	<K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapWithCache(
			BindingRoot<? super T, M> binding,
			SerializableSupplier2<? extends M> newMapSupplier,
			SerializableFunction2<? super K, ? extends IModel<K>> keyModelFunction,
			SerializableFunction2<? super V, ? extends IModel<V>> valueModelFunction
	);
	
	/**
	 * @return A previously created model collection tied to the given binding.
	 * @throws NoSuchModelException If no collection model was added for the given binding.
	 */
	<K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapAlreadyAdded(BindingRoot<? super T, M> binding);

	/**
	 * If this model is cached, writes the cache to the reference model.
	 */
	void write();

	/**
	 * If this model is cached (i.e. if the underlying model is a WorkingCopyModel), writes the cache to the reference model.
	 * <p>Also calls {@link #writeAll()} on all the models previously returned by {@link #bindWithCache(BindingRoot, IModel)},
	 * {@link #bindCollectionWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2)} or
	 * {@link #bindMapWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2, SerializableFunction2)}.
	 */
	void writeAll();

	/**
	 * If this model is cached (i.e. if the underlying model is a WorkingCopyModel), reads from the reference model to the cache.
	 * <p>Also calls {@link #readAll()} on all the models previously returned by {@link #bindWithCache(BindingRoot, IModel)},
	 * {@link #bindCollectionWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2)} or
	 * {@link #bindMapWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2, SerializableFunction2)}.
	 */
	void readAll();
	
	/**
	 * <p>Calls {@link #readAll()} on all the models previously returned by {@link #bindWithCache(BindingRoot, IModel)},
	 * {@link #bindCollectionWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2)} or
	 * {@link #bindMapWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2, SerializableFunction2)}.
	 */
	void readAllExceptMainModel();
	
	/**
	 * <p>Calls {@link #readAll()} on all the models previously returned by {@link #bindWithCache(BindingRoot, IModel)},
	 * {@link #bindCollectionWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2)} or
	 * {@link #bindMapWithCache(BindingRoot, SerializableSupplier2, SerializableFunction2, SerializableFunction2)}.
	 */
	<T2> void readAllUnder(BindingRoot<? super T, T2> binding);

	/**
	 * Calls {@link IModel#setObject(Object)} on the given model with the current model object as a parameter, and keeps
	 * a reference to the given model so that it will be returned by {@link #getInitialValueModel()}.
	 * <p>This allows to keep the initial value of this model in a separate model so that it may be retrieved later. 
	 */
	void setInitialValueModel(IModel<T> initialValueModel);
	
	/**
	 * @return The model previously assigned with {@link #setInitialValueModel(IModel)}, or <code>null</code> if
	 * there is none.
	 */
	IModel<T> getInitialValueModel();

}