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
	 * Clé : propriété pointée par le {@link BindableModel} (property).
	 * Valeur : {@link BindableModel} correspondant (peut être de type {@link BindableCollectionModel}
	 * ou {@link BindableMapModel})
	 * 
	 * <p>On utilise une linkedHashMap pour parcourir les propriétés (lors du writeAll() ou du readAll()) dans l'ordre
	 * de leur déclaration, ce qui permet à l'utilisateur de déclarer ".a" puis ".a.b"<br/>
	 * TODO YRO : corriger ça, il faudrait tout simplement créer un BindingModel pour a() dès la création du BindingModel
	 * pour ".a.b". On s'assurerait ainsi que les writeAll et readAll tiennent bien compte de la hiérarchie.
	 * 
	 * <p>Cette propriété est initialisée de manière paresseuse (lazy), car elle n'est pas toujours nécessaire.
	 */
	private Map<FieldPath, BindableModel<?>> propertyModels = Maps.newLinkedHashMap();
	
	/**
	 * <p>Cette propriété est initialisée de manière paresseuse (lazy), car elle n'est pas toujours nécessaire.
	 */
	private Multimap<FieldPath, BindableModel<?>> propertyModelsByImpactingPaths = LinkedHashMultimap.create();

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
			
			// Le WorkingCopyModel peut avoir été ajouté APRÈS un appel à bind(). On met à jour le DelegatingModel.
			propertyModel.setMainModel(workingCopyModel);
			
			registerImpactingPaths(path, propertyModel);
			
			return propertyModel;
		}
	}

	/**
	 * Enregistre les chemins impactants lors d'une modification, afin qu'on puisse effectuer des
	 * read() intelligemment sur le workingCopyModel lors d'une modification des données à l'initiative
	 * de LOG@RT. Cf. #readAllUnder()
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
		@SuppressWarnings("unchecked") // On est sûr des generics, par construction
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
			// TODO YRO faire en sorte de simplement remplacer le modèle existant par celui qu'on va créer
			// Cela nécessitera de :
			// 1. Modifier le mainModel du BindableModel créé précédemment
			// 2. Mettre en commun la map propriété => bindableModel du BindableModel créé précédemment et du
			//    nouveau BindableCollectionModel. Cela pourrait se faire plus simplement si on stockait toutes ces
			//    informations directement sur le BindingModel "racine".
			throw newNotCollectionModelException(path);
		} else {
			@SuppressWarnings("unchecked") // On fait un instanceof plus haut + on est certains des generics, par construction.
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
		@SuppressWarnings("unchecked") // On est sûrs des generics, par construction
		BindableModel<C> propertyModel = (BindableModel<C>) getPropertyModels().get(path);
		if (propertyModel == null) {
			throw new NoSuchModelException("No collection model was added for path '" + path + "'. Use bindCollectionWithCache in order to add a collection model.");
		} else if (!(propertyModel instanceof IBindableCollectionModel)) {
			throw newNotCollectionModelException(path);
		} else {
			@SuppressWarnings("unchecked") // On fait un instanceof plus haut + on est certains des generics, par construction.
			IBindableCollectionModel<T, C> collectionPropertyModel = (IBindableCollectionModel<T, C>) propertyModel;
			return collectionPropertyModel;
		}
	}
	
	@Override
	public <K, V, M extends Map<K, V>> IBindableMapModel<K, V, M> bindMapWithCache(BindingRoot<? super E, M> binding,
			Supplier<? extends M> newMapSupplier, Function<? super K, ? extends IModel<K>> keyModelFunction,
			Function<? super V, ? extends IModel<V>> valueModelFunction) {
		FieldPath path = FieldPath.fromBinding(binding);
		@SuppressWarnings("unchecked") // On est sûr des generics, par construction
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
			// TODO YRO faire en sorte de simplement remplacer le modèle existant par celui qu'on va créer
			// Cf bindCollectionWithCache pour plus d'information.
			throw newNotMapModelException(path);
		} else {
			@SuppressWarnings("unchecked") // On fait un instanceof plus haut + on est certains des generics, par construction.
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
		@SuppressWarnings("unchecked") // On est sûrs des generics, par construction
		IBindableModel<M> propertyModel = (IBindableModel<M>) getPropertyModels().get(path);
		if (propertyModel == null) {
			throw new NoSuchModelException("No map model was added for path '" + path + "'. Use bindMapWithCache in order to add a map model.");
		} else if (!(propertyModel instanceof IBindableMapModel)) {
			throw newNotMapModelException(path);
		} else {
			@SuppressWarnings("unchecked") // On fait un instanceof plus haut + on est certains des generics, par construction.
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
				&& getMainObject() != null // Wicket property resolver : Runtime exception when trying to set a value on a null object.
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
