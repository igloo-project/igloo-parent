package org.iglooproject.wicket.more.bindable.model;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.fieldpath.FieldPathPropertyComponent;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.bindable.exception.NoSuchModelException;
import org.iglooproject.wicket.more.model.WorkingCopyModel;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class BindableModel<E> implements IBindableModel<E> {
	
	private static final long serialVersionUID = -1361726182147235268L;

	@SuppressWarnings("rawtypes") // Works for any T
	private static enum BindableModelWrappingFunction implements SerializableFunction2<IModel, IBindableModel> {
		INSTANCE;
		@Override
		@SuppressWarnings("unchecked")
		public IBindableModel apply(IModel input) {
			return input instanceof IBindableModel ? (IBindableModel) input : new BindableModel(input);
		}
		@SuppressWarnings("unchecked")
		private static <T> SerializableFunction2<? super IModel<T>, ? extends IBindableModel<T>> get() {
			// We are absolutely sure that the returned IBindableModel will be a IBindableModel<T>
			// We do two casts here to prevent the javac compiler to raise an error (it's a hack)
			return (SerializableFunction2<? super IModel<T>, ? extends IBindableModel<T>>)
					(Object) BindableModelWrappingFunction.INSTANCE;
		}
	}
	
	public static <T> SerializableFunction2<? super T, ? extends IBindableModel<T>> factory(SerializableFunction2<? super T, ? extends IModel<T>> function) {
		return Functions2.compose(BindableModelWrappingFunction.<T>get(), function);
	}
	
	private IModel<E> mainModel;
	
	private IModel<E> initialValueModel;
	
	private transient boolean detaching = false;
	
	/**
	 * Map of bindable models for properties.
	 * <ul>
	 * <li>Key: the property itself, as a {@link FieldPath}.
	 * <li>Value: the {@link BindableModel} representing the property (which might be a {@link BindableCollectionModel} or
	 * a {@link BindableMapModel}.
	 * 
	 * <p>This attribute is lazily-initialized in {@link #getPropertyModels()}.
	 */
	private Map<FieldPath, BindableModel<?>> propertyModels;
	
	/**
	 * Map of bindable models impacted when one path is updated.
	 * 
	 * <p>This attribute is lazily-initialized in {@link #getPropertyModelsByImpactingPaths()}.
	 */
	private Multimap<FieldPath, BindableModel<?>> propertyModelsByImpactingPaths;

	public BindableModel(IModel<E> mainModel) {
		super();
		this.mainModel = mainModel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BindableModel)) {
			return false;
		}
		BindableModel<?> other = (BindableModel<?>) obj;
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
	public E getObject() {
		return mainModel.getObject();
	}

	@Override
	public void setObject(E object) {
		getDelegateModel().setObject(object);
		readAllExceptMainModel();
	}
	
	private IModel<E> getDelegateModel() {
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
	
	protected boolean hasCache() {
		return mainModel instanceof WorkingCopyModel;
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
					propertyModel.getDelegateModel(),
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
			SerializableSupplier2<? extends C> newCollectionSupplier,
			SerializableFunction2<? super T, ? extends IModel<T>> itemModelFunction
	) {
		FieldPath path = FieldPath.fromBinding(binding);
		return bindCollectionWithCache(this, path, path, newCollectionSupplier, itemModelFunction);
	}

	private <T, C extends Collection<T>> IBindableCollectionModel<T, C> bindCollectionWithCache(
			BindableModel<?> rootBindableModel,
			FieldPath originalPath,
			FieldPath path,
			SerializableSupplier2<? extends C> newCollectionSupplier,
			SerializableFunction2<? super T, ? extends IModel<T>> itemModelFunction
	) {
		BindableModel<?> owner = getOrCreateSimpleModel(path.parent().get());
		if (owner != this) {
			return owner.bindCollectionWithCache(
					rootBindableModel, originalPath,
					path.relativeToParent().get(), newCollectionSupplier, itemModelFunction
			);
		}
		
		@SuppressWarnings("unchecked") // Generic parameters are known, by construction
		BindableModel<C> propertyModel = (BindableModel<C>) getPropertyModels().get(path);
		if (propertyModel == null) {
			BindableCollectionModel<T, C> collectionPropertyModel = new BindableCollectionModel<>(
					this.<C>createBindingModel(getDelegateModel(), path),
					newCollectionSupplier, itemModelFunction
			);
			getPropertyModels().put(path, collectionPropertyModel);
			
			rootBindableModel.registerImpactingPaths(originalPath, collectionPropertyModel);
			
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
		return bindCollectionAlreadyAdded(path);
	}
	
	private <T, C extends Collection<T>> IBindableCollectionModel<T, C> bindCollectionAlreadyAdded(FieldPath path) {
		BindableModel<?> owner = getOrCreateSimpleModel(path.parent().get());
		if (owner != this) {
			return owner.bindCollectionAlreadyAdded(path.relativeToParent().get());
		}
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
	public <K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapWithCache(
			BindingRoot<? super E, M> binding,
			SerializableSupplier2<? extends M> newMapSupplier,
			SerializableFunction2<? super K, ? extends IModel<K>> keyModelFunction,
			SerializableFunction2<? super V, ? extends IModel<V>> valueModelFunction
	) {
		FieldPath path = FieldPath.fromBinding(binding);
		return bindMapWithCache(this, path, path, newMapSupplier, keyModelFunction, valueModelFunction);
	}
	
	private <K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapWithCache(
			BindableModel<?> rootBindableModel,
			FieldPath originalPath, FieldPath path,
			SerializableSupplier2<? extends M> newMapSupplier,
			SerializableFunction2<? super K, ? extends IModel<K>> keyModelFunction,
			SerializableFunction2<? super V, ? extends IModel<V>> valueModelFunction
	) {
		BindableModel<?> owner = getOrCreateSimpleModel(path.parent().get());
		if (owner != this) {
			return owner.bindMapWithCache(
					rootBindableModel, originalPath,
					path.relativeToParent().get(), newMapSupplier, keyModelFunction, valueModelFunction
			);
		}
		@SuppressWarnings("unchecked") // Generic parameters are known, by construction
		BindableModel<M> propertyModel = (BindableModel<M>) getPropertyModels().get(path);
		if (propertyModel == null) {
			BindableMapModel<K, V, M> mapPropertyModel = new BindableMapModel<>(
					this.<M>createBindingModel(getDelegateModel(), path),
					newMapSupplier, keyModelFunction, valueModelFunction
			);
			getPropertyModels().put(path, mapPropertyModel);
			
			rootBindableModel.registerImpactingPaths(originalPath, mapPropertyModel);
			
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
		return bindMapAlreadyAdded(path);
	}

	private <K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapAlreadyAdded(FieldPath path) {
		BindableModel<?> owner = getOrCreateSimpleModel(path.parent().get());
		if (owner != this) {
			return owner.bindMapAlreadyAdded(path.relativeToParent().get());
		}
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
	private <T> BindableModel<T> getOrCreateSimpleModel(BindingRoot<? super E, T> binding, FieldPath path) {
		return getOrCreateSimpleModel(path);
	}

	@SuppressWarnings("unchecked")
	private <T> BindableModel<T> getOrCreateSimpleModel(FieldPath path) {
		if (path.isRoot()) {
			// Binding the root, i.e. this
			return (BindableModel<T>) this;
		} else if (path.parent().get().isRoot()) {
			// Binding a direct child property
			BindableModel<T> propertyModel = (BindableModel<T>) getPropertyModels().get(path);
			if (propertyModel == null) {
				propertyModel = new BindableModel<>(this.<T>createBindingModel(getDelegateModel(), path));
				getPropertyModels().put(path, propertyModel);
			}
			return propertyModel;
		} else {
			/*
			 * Binding an indirect child property
			 * We make sure to never create a "shortcut" that would skip an intermediary model, because
			 * it would allow users to create multiple BindableModels pointing to the same property.
			 */
			FieldPath parentPath = path.parent().get();
			FieldPath relativePropertyPath = path.relativeTo(parentPath).get();
			return getOrCreateSimpleModel(parentPath)
					.getOrCreateSimpleModel(relativePropertyPath);
		}
	}
	
	private <T> AbstractPropertyModel<T> createBindingModel(IModel<E> delegateModel, FieldPath path) {
		final String propertyExpression = StringUtils.trimLeadingCharacter(
				path.toString(), FieldPathPropertyComponent.PROPERTY_SEPARATOR_CHAR
		);
		return new PropertyModel<>(delegateModel, propertyExpression);
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
				&& getObject() != null
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
	public final void detach() {
		if (!detaching) {
			detaching = true;
			try {
				try {
					onDetach();
				} finally {
					mainModel.detach();
					Detachables.detach(initialValueModel);
					if (propertyModels != null) {
						Detachables.detach(propertyModels.values());
					}
				}
			} finally {
				detaching = false;
			}
		}
	}

	protected void onDetach() {
		// To be overridden by subclasses
	}

}
