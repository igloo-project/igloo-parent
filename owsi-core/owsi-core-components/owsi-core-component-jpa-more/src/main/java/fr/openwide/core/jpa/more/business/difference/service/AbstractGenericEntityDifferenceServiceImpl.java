package fr.openwide.core.jpa.more.business.difference.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.bindgen.BindingRoot;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import de.danielbechler.diff.NodeQueryService;
import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.differ.Differ;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.differ.DifferFactory;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.inclusion.InclusionResolver;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.Visitor;
import de.danielbechler.diff.node.Visit;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.business.generic.service.ITransactionScopeIndependantRunnerService;
import fr.openwide.core.jpa.more.business.difference.differ.ExtendedCollectionDiffer;
import fr.openwide.core.jpa.more.business.difference.differ.MultimapDiffer;
import fr.openwide.core.jpa.more.business.difference.factory.DefaultHistoryDifferenceFactory;
import fr.openwide.core.jpa.more.business.difference.factory.IHistoryDifferenceFactory;
import fr.openwide.core.jpa.more.business.difference.inclusion.NonInheritingNodePathInclusionResolver;
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.difference.util.CompositeProxyInitializer;
import fr.openwide.core.jpa.more.business.difference.util.DiffUtils;
import fr.openwide.core.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import fr.openwide.core.jpa.more.business.difference.util.IProxyInitializer;
import fr.openwide.core.jpa.more.business.difference.util.TypeSafeBindingProxyInitializer;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPathComponent;
import fr.openwide.core.jpa.util.HibernateUtils;

@SuppressFBWarnings("squid:S1226")
public abstract class AbstractGenericEntityDifferenceServiceImpl<T extends GenericEntity<?, ?>> implements IDifferenceService<T> {
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IRendererService rendererService;
	
	@Autowired
	private ITransactionScopeIndependantRunnerService transactionScopeIndependantRunnerService;
	
	@Autowired
	private DefaultHistoryDifferenceFactory<T> defaultHistoryDifferenceFactory;
	
	private IProxyInitializer<T> proxyInitializer;	
	private Multimap<FieldPath, IHistoryDifferenceFactory<T>> specificHistoryDifferenceFactories;

	@SuppressWarnings("rawtypes")
	private List<Class<? extends GenericEntity>> genericEntityTypes;

	private IDifferenceFromReferenceGenerator<T> mainDifferenceGenerator;

	private IDifferenceFromReferenceGenerator<T> minimalDifferenceGenerator;

	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void initialize() {
		this.genericEntityTypes = entityService.listAssignableEntityTypes(GenericEntity.class);
		
		this.mainDifferenceGenerator = new AbstractDifferenceFromReferenceGenerator() {
			@Override
			protected ObjectDiffer createDiffer() {
				return initializeDiffer(DiffUtils.builder()).build();
			}
		};
		
		this.minimalDifferenceGenerator = new AbstractDifferenceFromReferenceGenerator() {
			@Override
			protected ObjectDiffer createDiffer() {
				return initializeMinimalDiffer(DiffUtils.builder()).build();
			}
		};
		
		List<IProxyInitializer<? super T>> initializers = Lists.newArrayList();
		
		// Initialisation des champs simples
		Iterable<? extends AbstractCoreBinding<? extends T, ?>> simpleFieldsBindingsList = getSimpleInitializationFieldsBindings();
		initializers.add(new TypeSafeBindingProxyInitializer<T>(simpleFieldsBindingsList));
		
		// Initialisations personnalisées
		Iterables.addAll(initializers, initializeInitializers());
		
		this.proxyInitializer = new CompositeProxyInitializer<T>(initializers);

		// Création personnalisée des AuditDifference
		ImmutableMultimap.Builder<FieldPath, IHistoryDifferenceFactory<T>> factoriesMapBuilder = ImmutableMultimap.builder();
		Multimap<IHistoryDifferenceFactory<T>, FieldPath> specificAuditDifferenceFactoriesToFieldPaths = getSpecificAuditDifferenceFactories();
		for (Entry<IHistoryDifferenceFactory<T>, FieldPath> entry : specificAuditDifferenceFactoriesToFieldPaths.entries()) {
			factoriesMapBuilder.putAll(entry.getValue(), entry.getKey());
		}
		specificHistoryDifferenceFactories = factoriesMapBuilder.build();
	}

	protected Multimap<IHistoryDifferenceFactory<T>, FieldPath> getSpecificAuditDifferenceFactories() {
		return ImmutableMultimap.<IHistoryDifferenceFactory<T>, FieldPath>of();
	}
	
	protected abstract Iterable<? extends AbstractCoreBinding<? extends T, ?>> getSimpleInitializationFieldsBindings();

	protected ObjectDifferBuilder initializeDiffer(ObjectDifferBuilder builder) {
		for (@SuppressWarnings("rawtypes") Class<? extends GenericEntity> clazz : genericEntityTypes) {
			builder = builder.inclusion().exclude()
					.propertyNameOfType(clazz, "new", "displayName", "id", "nameForToString")
					.and();
		}
		
		builder = builder.differs().register(new DifferFactory() {
			@Override
			public Differ createDiffer(DifferDispatcher differDispatcher, NodeQueryService nodeQueryService) {
				ExtendedCollectionDiffer differ = new ExtendedCollectionDiffer(differDispatcher, nodeQueryService, nodeQueryService);
				return initializeCollectionDiffer(differ);
			}
		});
		
		builder = builder.differs().register(new DifferFactory() {
			@Override
			public Differ createDiffer(DifferDispatcher differDispatcher, NodeQueryService nodeQueryService) {
				MultimapDiffer differ = new MultimapDiffer(differDispatcher, nodeQueryService, nodeQueryService);
				return initializeMultimapDiffer(differ);
			}
		});
		
		return builder;
	}
	
	protected Iterable<? extends BindingRoot<? super T, ?>> getMinimalDifferenceFieldsBindings() {
		// Par défaut, le diff minimal n'inclut aucun noeud
		return ImmutableList.<BindingRoot<? super T, ?>>of();
	}

	protected final ObjectDifferBuilder initializeMinimalDiffer(ObjectDifferBuilder builder) {
		builder = initializeDiffer(builder);
		
		// Permet d'inclure un noeud sans que tous ses enfants soient également inclus
		NonInheritingNodePathInclusionResolver parentInclusionResolver = new NonInheritingNodePathInclusionResolver();
		builder = builder.inclusion().resolveUsing(parentInclusionResolver).and();
		
		// On s'assure que, si aucun noeud n'a été spécifié comme inclus, les autres noeuds ne seront pas
		// considérés comme inclus "par défaut"
		builder = builder.inclusion().resolveUsing(new InclusionResolver() {
			@Override
			public Inclusion getInclusion(DiffNode node) {
				return Inclusion.DEFAULT; // Ne se prononce pas
			}
			@Override
			public boolean enablesStrictIncludeMode() {
				return true;
			}
		}).and();
		
		
		for (BindingRoot<? super T, ?> binding : getMinimalDifferenceFieldsBindings()) {
			FieldPath path = FieldPath.fromBinding(binding);
			
			// Le noeud, ainsi que tous ses enfants, sont inclus
			builder = builder.inclusion().include().node(DiffUtils.toNodePath(path))
					.and();
			
			// Pour que ça fonctionne, on doit également inclure les éventuels parents
			// Cependant, on n'utilise pas le système de catégorie ici, ni le NodePathInclusionResolver,
			// puisque ça reviendrait à inclure tous les enfants du parent (les catégories sont héritées par les enfants,
			// et le NodePathInclusionResolver part du principe qu'on inclut tous les enfaants d'un noed inclus).
			path = path.parent().get();
			while (!path.isRoot()) {
				parentInclusionResolver.setInclusion(DiffUtils.toNodePath(path), Inclusion.INCLUDED);
				path = path.parent().get();
			}
		}
		
		return builder;
	}
	
	protected Iterable<? extends IProxyInitializer<? super T>> initializeInitializers() {
		return Collections.emptyList();
	}

	protected ExtendedCollectionDiffer initializeCollectionDiffer(ExtendedCollectionDiffer differ) {
		return differ;
	}

	protected MultimapDiffer initializeMultimapDiffer(MultimapDiffer differ) {
		return differ;
	}
	
	@Override
	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> toHistoryDifferences(final Supplier<HD> historyDifferenceSupplier, final Difference<T> rootDifference) {
		final Multimap<IHistoryDifferenceFactory<T>, DiffNode> factoriesToNodes = LinkedHashMultimap.create();
		
		// Liste les noeud "feuilles", en attribuant au passage les noeuds à des Factory spécifiques le cas échéant.
		// Une "feuille" est soit un noeud qui n'a pas d'enfant, soit un noeud correspondant à un élément de Collection ou de Map.
		rootDifference.getDiffNode().visitChildren(new Visitor() {
			private Deque<FieldPathComponent> pathComponents = new LinkedList<>();
			@Override
			public void node(DiffNode node, Visit visit) {
				visit.dontGoDeeper();
				FieldPathComponent component = DiffUtils.toFieldPathComponent(node.getPath().getLastElementSelector());
				pathComponents.addLast(component);
				Collection<IHistoryDifferenceFactory<T>> specificFactories = specificHistoryDifferenceFactories.get(FieldPath.of(pathComponents));
				if (!specificFactories.isEmpty()) {
					for (IHistoryDifferenceFactory<T> specificFactory : specificFactories) {
						factoriesToNodes.put(specificFactory, node);
					}
				} else {
					if (component == FieldPathComponent.ITEM || !node.hasChildren()) {
						factoriesToNodes.put(defaultHistoryDifferenceFactory, node);
					} else {
						node.visitChildren(this);
					}
				}
				pathComponents.removeLast();
			}
		});
		
		final List<HD> historyDifferences = Lists.newLinkedList();
		try {
			rendererService.runWithContext(new Callable<Void>() {
				@Override
				public Void call() {
					for (Map.Entry<IHistoryDifferenceFactory<T>, Collection<DiffNode>> entry : factoriesToNodes.asMap().entrySet()) {
						IHistoryDifferenceFactory<T> factory = entry.getKey();
						Collection<DiffNode> nodes = entry.getValue();
						// XXX HistoryLog : à corriger
						historyDifferences.addAll(factory.create(historyDifferenceSupplier, rootDifference, nodes));
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected exception while computing HistoryDifferences", e);
		}
		return historyDifferences;
	}
	
	private abstract class AbstractDifferenceFromReferenceGenerator implements IDifferenceFromReferenceGenerator<T> {
		@Override
		public Difference<T> diff(T modifie, T reference) {
			return new Difference<T>(reference, modifie, createDiffer().compare(modifie, reference));
		}

		/**
		 * Crée un differ.
		 * <p>Le differ doit être instancié à chaque fois qu'on a besoin de lui, parce qu'il n'est pas thread-safe.
		 * <p>De plus, il semble avoir un état interne qui n'est pas forcément bien nettoyé en cas d'erreur...
		 */
		protected abstract ObjectDiffer createDiffer();
		
		@Override
		public Difference<T> diffFromReference(T after) {
			T before = transactionScopeIndependantRunnerService.run(getReferenceProviderAndInitializer(after));
			return diff(after, before);
		}
		
		private Callable<T> getReferenceProviderAndInitializer(T value) {
			final GenericEntityReference<?, T> reference = GenericEntityReference.ofUnknownIdType(value);
			return new Callable<T>() {
				@Override
				public T call() throws Exception {
					if (reference == null) {
						return null;
					} else {
						T databaseVersion = HibernateUtils.unwrap(entityService.getEntity(reference));
						proxyInitializer.initialize(databaseVersion);
						return databaseVersion;
					}
				}
			};
		}
		
		@Override
		public Callable<T> getReferenceProvider(T value) {
			final GenericEntityReference<?, T> reference = GenericEntityReference.ofUnknownIdType(value);
			return new Callable<T>() {
				@Override
				public T call() throws Exception {
					if (reference == null) {
						return null;
					} else {
						T databaseVersion = entityService.getEntity(reference);
						return HibernateUtils.unwrap(databaseVersion);
					}
				}
			};
		}
		
		@Override
		public void initializeReference(T reference) {
			proxyInitializer.initialize(reference);
		}
	}
	
	@Override
	public IDifferenceFromReferenceGenerator<T> getMainDifferenceGenerator() {
		return mainDifferenceGenerator;
	}
	
	@Override
	public IDifferenceFromReferenceGenerator<T> getMinimalDifferenceGenerator() {
		return minimalDifferenceGenerator;
	}
}
