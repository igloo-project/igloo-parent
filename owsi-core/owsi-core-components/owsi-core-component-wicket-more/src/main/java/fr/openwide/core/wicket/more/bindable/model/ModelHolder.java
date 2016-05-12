package fr.openwide.core.wicket.more.bindable.model;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.wicket.more.bindable.exception.NoSuchModelException;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.WorkingCopyModel;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class ModelHolder<E> implements IBindable<E> {
	
	private static final long serialVersionUID = -1361726182147235268L;
	
	private IModel<E> mainModel;
	
	private IModel<E> initialValueModel;
	
	/**
	 * Map of bindable models for properties.
	 * <ul>
	 * <li>Key: the property itself, as a {@link FieldPath}.
	 * <li>Value: the {@link BindableModel} representing the property (which might be a {@link BindableCollectionModel} or
	 * a {@link BindableMapModel}.
	 * 
	 * <p>This attribute is lazily-initialized in {@link #getPropertyModels()}.
	 * 
	 * <p>We use a {@link java.util.LinkedHashMap}, so that when we have to run through the properties
	 * (in {@link #writeAll()} or {@link #readAll()} for instance), we do it in the same order they were declared.
	 * This allows users to first declare <pre>.a</pre> and then <pre>.a.b</pre>, so that <pre>.a</pre> is
	 * read/written <em>before</em> <pre>.a.b</pre>.
	 * <p>TODO YRO: fix this. We should simply ensure that we create a model for a non-direct property (<pre>.a.b</pre>),
	 * then we first (implicitely) create the model for the intermediate properties (<pre>.a</pre> in this example),
	 * and ask <em>this</em> new model to provide the model for <pre>.a.b</pre>.
	 * That way, {@link #writeAll()} and {@link #readAll()} would always take the hierarchical order of the properties
	 * into account.
	 */
	private Map<FieldPath, BindableModel<?>> propertyModels;
	
	/**
	 * Map of bindable models impacted when one path is updated.
	 * 
	 * <p>This attribute is lazily-initialized in {@link #getPropertyModelsByImpactingPaths()}.
	 */
	private Multimap<FieldPath, BindableModel<?>> propertyModelsByImpactingPaths;

	public ModelHolder(IModel<E> mainModel) {
		super();
		this.mainModel = mainModel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ModelHolder)) {
			return false;
		}
		ModelHolder<?> other = (ModelHolder<?>) obj;
		return new EqualsBuilder()
				.append(mainModel, other.mainModel)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(mainModel)
				.build();
	}
	
	@Override
	public IModel<E> getMainModel() {
		return mainModel;
	}
	
	protected final IModel<E> getMainModelInternal() {
		return mainModel;
	}
	
	protected final void setMainModel(IModel<E> mainModel) {
		this.mainModel = mainModel;
	}

	@Override
	public IModel<E> getInitialValueModel() {
		return this.initialValueModel;
	}
	
	@Override
	public void setInitialValueModel(IModel<E> initialValueModel) {
		this.initialValueModel = initialValueModel;
		this.initialValueModel.setObject(this.mainModel.getObject());
	}
	
	private Map<FieldPath, BindableModel<?>> getPropertyModels() {
		if (propertyModels == null) {
			propertyModels = Maps.newLinkedHashMap();
		}
		return propertyModels;
	}
	
	private Multimap<FieldPath, BindableModel<?>> getPropertyModelsByImpactingPaths() {
		if (propertyModelsByImpactingPaths == null) {
			propertyModelsByImpactingPaths = LinkedHashMultimap.create();
		}
		return propertyModelsByImpactingPaths;
	}
	
	@Override
	public boolean hasCache() {
		return mainModel instanceof WorkingCopyModel;
	}
	
	public E getMainObject() {
		return mainModel.getObject();
	}

	@Override
	public <T> IBindableModel<T> bind(BindingRoot<? super E, T> binding) {
		return getOrCreateSimpleModel(binding, FieldPath.fromBinding(binding));
	}

	@Override
	public <T> IBindableModel<T> bindWithCache(BindingRoot<? super E, T> binding, IModel<T> workingCopyProposal) {
		FieldPath path = FieldPath.fromBinding(binding);
		BindableModel<T> propertyModel = getOrCreateSimpleModel(binding, path);
		if (propertyModel.hasCache()) {
			return propertyModel;
		} else {
			WorkingCopyModel<T> workingCopyModel = WorkingCopyModel.of(
					propertyModel.getMainModelInternal(),
					workingCopyProposal
			);
			
			/* Add a cache to the property model, which may or may not have already existed before we called
			 * getOrCreateSimpleModel().
			 */
			propertyModel.setMainModel(workingCopyModel);
			
			registerImpactingPaths(path, propertyModel);
			
			return propertyModel;
		}
	}

	/**
	 * Records the impacting paths, i.e. the paths such that when we read the cache on them (respectively, write),
	 * then the given model should be read (respectively, written), too.
	 * 
	 * @see #readAllUnder(BindingRoot)
	 */
	private void registerImpactingPaths(FieldPath path, BindableModel<?> model) {
		FieldPath currentPath = path;
		do {
			getPropertyModelsByImpactingPaths().put(currentPath, model);
			currentPath = currentPath.parent().get();
		} while (!currentPath.isRoot());
	}

	@Override
	public <T, C extends Collection<T>> IBindableCollectionModel<T, C> bindCollectionWithCache(
			BindingRoot<? super E, C> binding,
			Supplier<? extends C> newCollectionSupplier,
			Function<? super T, ? extends IModel<T>> itemModelFunction) {
		FieldPath path = FieldPath.fromBinding(binding);
		@SuppressWarnings("unchecked") // Generic parameters are known, by construction
		BindableModel<C> propertyModel = (BindableModel<C>) getPropertyModels().get(path);
		if (propertyModel == null) {
			BindableCollectionModel<T, C> collectionPropertyModel = new BindableCollectionModel<>(
					BindingModel.of(getMainModel(), binding),
					newCollectionSupplier, itemModelFunction
			);
			getPropertyModels().put(path, collectionPropertyModel);
			
			registerImpactingPaths(path, collectionPropertyModel);
			
			return collectionPropertyModel;
		} else if (!(propertyModel instanceof IBindableCollectionModel)) {
			// TODO YRO support the case when we first called bind(), then bindCollectionWithCache()
			throw newNotCollectionModelException(path);
		} else {
			@SuppressWarnings("unchecked") // Generic parameters are known, by construction.
			IBindableCollectionModel<T, C> collectionPropertyModel = (IBindableCollectionModel<T, C>) propertyModel;
			return collectionPropertyModel;
		}
	}
	
	private static IllegalStateException newNotCollectionModelException(FieldPath path) {
		return new IllegalStateException(
				"The model bound to property '" + path + "' was registered using bind() or bindWithCache(). Thus, it"
				+ " does not not implement IBindableCollectionModel. This state cannot be reversed."
				+ " You should check your code and make sure you will always call bindCollectionWithCache() first,"
				+ " before any call to bind() or bindWithCache() for the same property."
		);
	}
	
	@Override
	public <T, C extends Collection<T>> IBindableCollectionModel<T, C> bindCollectionAlreadyAdded(BindingRoot<? super E, C> binding) {
		FieldPath path = FieldPath.fromBinding(binding);
		@SuppressWarnings("unchecked") // Generic parameters are known, by construction
		BindableModel<C> propertyModel = (BindableModel<C>) getPropertyModels().get(path);
		if (propertyModel == null) {
			throw new NoSuchModelException("No collection model was added for path '" + path
					+ "'. Use bindCollectionWithCache in order to add a collection model.");
		} else if (!(propertyModel instanceof IBindableCollectionModel)) {
			throw newNotCollectionModelException(path);
		} else {
			@SuppressWarnings("unchecked") // Generic parameters are known, by construction
			IBindableCollectionModel<T, C> collectionPropertyModel = (IBindableCollectionModel<T, C>) propertyModel;
			return collectionPropertyModel;
		}
	}
	
	@Override
	public <K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapWithCache(BindingRoot<? super E, M> binding,
			Supplier<? extends M> newMapSupplier, Function<? super K, ? extends IModel<K>> keyModelFunction,
			Function<? super V, ? extends IModel<V>> valueModelFunction) {
		FieldPath path = FieldPath.fromBinding(binding);
		@SuppressWarnings("unchecked") // Generic parameters are known, by construction
		BindableModel<M> propertyModel = (BindableModel<M>) getPropertyModels().get(path);
		if (propertyModel == null) {
			BindableMapModel<K, V, M> mapPropertyModel = new BindableMapModel<>(
					BindingModel.of(getMainModel(), binding),
					newMapSupplier, keyModelFunction, valueModelFunction
			);
			getPropertyModels().put(path, mapPropertyModel);
			
			registerImpactingPaths(path, mapPropertyModel);
			
			return mapPropertyModel;
		} else if (!(propertyModel instanceof IBindableMapModel)) {
			// TODO YRO support the case when we first called bind(), then bindMapWithCache()
			throw newNotMapModelException(path);
		} else {
			@SuppressWarnings("unchecked") // Generic parameters are known, by construction
			IBindableMapModel<K, V, M> mapPropertyModel = (IBindableMapModel<K, V, M>) propertyModel;
			return mapPropertyModel;
		}
	}
	
	private static IllegalStateException newNotMapModelException(FieldPath path) {
		return new IllegalStateException(
				"The model bound to property '" + path + "' was registered using bind() or bindWithCache(). Thus, it"
				+ " does not not implement IBindableMapModel. This state cannot be reversed."
				+ " You should check your code and make sure you will always call bindMapWithCache() first,"
				+ " before any call to bind() or bindWithCache() for the same property."
		);
	}

	@Override
	public <K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapAlreadyAdded(BindingRoot<? super E, M> binding) {
		FieldPath path = FieldPath.fromBinding(binding);
		@SuppressWarnings("unchecked") // Generic parameters are known, by construction
		IBindableModel<M> propertyModel = (IBindableModel<M>) getPropertyModels().get(path);
		if (propertyModel == null) {
			throw new NoSuchModelException("No map model was added for path '" + path
					+ "'. Use bindMapWithCache in order to add a map model.");
		} else if (!(propertyModel instanceof IBindableMapModel)) {
			throw newNotMapModelException(path);
		} else {
			@SuppressWarnings("unchecked") // Generic parameters are known, by construction
			IBindableMapModel<K, V, M> mapPropertyModel = (IBindableMapModel<K, V, M>) propertyModel;
			return mapPropertyModel;
		}
	}
	
	/**
	 * Retrieves a pre-existing BindableModel, or create it if necessary.
	 * <p>If creation is necessary, the created model will be an instance of {@link BindableModel},
	 * not {@link BindableCollectionModel} or {@link BindableMapModel}.
	 */
	@SuppressWarnings("unchecked")
	private <T> BindableModel<T> getOrCreateSimpleModel(BindingRoot<? super E, T> binding, FieldPath path) {
		BindableModel<T> propertyModel = (BindableModel<T>) getPropertyModels().get(path);
		if (propertyModel == null) {
			propertyModel = new BindableModel<>(BindingModel.of(getMainModel(), binding));
			getPropertyModels().put(path, propertyModel);
		}
		return propertyModel;
	}

	@Override
	public final void write() {
		if (hasCache()) {
			((WorkingCopyModel<?>)mainModel).write();
		}
	}

	@Override
	public final void writeAll() {
		write();
		if (propertyModels != null
				// Avoid an error in Wicket property resolver ("Runtime exception when trying to set a value on a null object.")
				&& getMainObject() != null
				) {
			for (IBindableModel<?> propertyModel : propertyModels.values()) {
				propertyModel.writeAll();
			}
		}
		onWriteAll();
	}

	protected void onWriteAll() {
		// nothing to do
	}

	@Override
	public final void readAll() {
		if (hasCache()) {
			((WorkingCopyModel<?>)mainModel).read();
		}
		readAllExceptMainModel();
	}

	@Override
	public final void readAllExceptMainModel() {
		if (propertyModels != null) {
			for (IBindableModel<?> propertyModel : propertyModels.values()) {
				propertyModel.readAll();
			}
		}
		onReadAll();
	}

	protected void onReadAll() {
		// nothing to do
	}

	@Override
	public final <T2> void readAllUnder(BindingRoot<? super E, T2> binding) {
		if (propertyModelsByImpactingPaths != null) {
			for (BindableModel<?> propertyModel : propertyModelsByImpactingPaths.get(FieldPath.fromBinding(binding))) {
				propertyModel.readAll();
			}
		}
	}

	@Override
	public void detach() {
		mainModel.detach();
		Detachables.detach(initialValueModel);
		if (propertyModels != null) {
			Detachables.detach(propertyModels.values());
		}
	}

}
