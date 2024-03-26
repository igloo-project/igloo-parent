package org.iglooproject.jpa.more.business.difference.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.bindgen.BindingRoot;
import org.hibernate.proxy.HibernateProxy;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.fieldpath.FieldPathComponent;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.business.generic.service.ITransactionScopeIndependantRunnerService;
import org.iglooproject.jpa.more.business.difference.differ.ExtendedCollectionDiffer;
import org.iglooproject.jpa.more.business.difference.differ.MultimapDiffer;
import org.iglooproject.jpa.more.business.difference.factory.DefaultHistoryDifferenceFactory;
import org.iglooproject.jpa.more.business.difference.factory.IHistoryDifferenceFactory;
import org.iglooproject.jpa.more.business.difference.inclusion.NonInheritingNodePathInclusionResolver;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.util.CompositeProxyInitializer;
import org.iglooproject.jpa.more.business.difference.util.DiffUtils;
import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IProxyInitializer;
import org.iglooproject.jpa.more.business.difference.util.TypeSafeBindingProxyInitializer;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
import de.danielbechler.diff.instantiation.TypeInfo;
import de.danielbechler.diff.introspection.Introspector;
import de.danielbechler.diff.introspection.StandardIntrospector;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.Visitor;
import de.danielbechler.diff.node.Visit;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import igloo.difference.AbstractConfiguredDifferenceServiceImpl;
import jakarta.annotation.PostConstruct;

/**
 * @since 6.0.0
 * @deprecated Use {@link AbstractConfiguredDifferenceServiceImpl}.
 */
@Deprecated(since = "6.0.0")
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
		
		// Initialization of the simple fields
		Iterable<? extends ICoreBinding<? extends T, ?>> simpleFieldsBindingsList = getSimpleInitializationFieldsBindings();
		initializers.add(new TypeSafeBindingProxyInitializer<T>(simpleFieldsBindingsList));
		
		// Customized initializations
		Iterables.addAll(initializers, initializeInitializers());
		
		this.proxyInitializer = new CompositeProxyInitializer<>(initializers);

		// Customized creation of the HistoryDifference items
		ImmutableMultimap.Builder<FieldPath, IHistoryDifferenceFactory<T>> factoriesMapBuilder = ImmutableMultimap.builder();
		Multimap<IHistoryDifferenceFactory<T>, FieldPath> specificHistoryDifferenceFactoriesToFieldPaths = getSpecificHistoryDifferenceFactories();
		for (Entry<IHistoryDifferenceFactory<T>, FieldPath> entry : specificHistoryDifferenceFactoriesToFieldPaths.entries()) {
			factoriesMapBuilder.putAll(entry.getValue(), entry.getKey());
		}
		specificHistoryDifferenceFactories = factoriesMapBuilder.build();
	}

	protected Multimap<IHistoryDifferenceFactory<T>, FieldPath> getSpecificHistoryDifferenceFactories() {
		return ImmutableMultimap.<IHistoryDifferenceFactory<T>, FieldPath>of();
	}
	
	protected abstract Iterable<? extends ICoreBinding<? extends T, ?>> getSimpleInitializationFieldsBindings();

	protected ObjectDifferBuilder initializeDiffer(ObjectDifferBuilder builder) {
		// Ignore Hibernate proxies fields
		builder = builder.introspection()
				.setDefaultIntrospector(new Introspector() {
					private Introspector delegate = new StandardIntrospector();
					@Override
					public TypeInfo introspect(Class<?> type) {
						if (HibernateProxy.class.isAssignableFrom(type)) {
							type = type.getSuperclass();
						}
						return delegate.introspect(type);
					}
				})
				.and();

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
		// By default, the minimal diff does not include any nodes
		return ImmutableList.<BindingRoot<? super T, ?>>of();
	}

	protected final ObjectDifferBuilder initializeMinimalDiffer(ObjectDifferBuilder builder) {
		builder = initializeDiffer(builder);
		
		// Allows to include a node without having all its children included
		NonInheritingNodePathInclusionResolver parentInclusionResolver = new NonInheritingNodePathInclusionResolver();
		builder = builder.inclusion().resolveUsing(parentInclusionResolver).and();
		
		// We make sure, that if no nodes have been specified as included, all the other nodes won't be considered
		// as included "by default"
		builder = builder.inclusion().resolveUsing(new InclusionResolver() {
			@Override
			public Inclusion getInclusion(DiffNode node) {
				return Inclusion.DEFAULT; // Don't vote
			}
			@Override
			public boolean enablesStrictIncludeMode() {
				return true;
			}
		}).and();
		
		
		for (BindingRoot<? super T, ?> binding : getMinimalDifferenceFieldsBindings()) {
			FieldPath path = FieldPath.fromBinding(binding);
			
			// The node and all its children are included
			builder = builder.inclusion().include().node(DiffUtils.toNodePath(path))
					.and();
			
			// For it to work, we also need to include the potential parents.
			// However we don't use the category system here nor the NodePathInclusionResolver because it would include
			// all the children of the parent (the categories are inherited by the children and the NodePathInclusionResolver
			// considers that we include all the children of a node.
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
	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> toHistoryDifferences(
			final Supplier2<HD> historyDifferenceSupplier, final Difference<T> rootDifference) {
		final Multimap<IHistoryDifferenceFactory<T>, DiffNode> factoriesToNodes = LinkedHashMultimap.create();
		
		// Lists the leaf nodes and attributes the nodes to specific factories if needed.
		// A "leaf" is either a node without children or a node which is an element of a Collection or a Map.
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
		try (ITearDownHandle handle = rendererService.context().open()) {
			for (Map.Entry<IHistoryDifferenceFactory<T>, Collection<DiffNode>> entry : factoriesToNodes.asMap().entrySet()) {
				IHistoryDifferenceFactory<T> factory = entry.getKey();
				Collection<DiffNode> nodes = entry.getValue();
				historyDifferences.addAll(factory.create(historyDifferenceSupplier, rootDifference, nodes));
			}
		} catch (RuntimeException e) {
			throw new IllegalStateException("Unexpected exception while computing HistoryDifferences", e);
		}
		return historyDifferences;
	}
	
	private abstract class AbstractDifferenceFromReferenceGenerator implements IDifferenceFromReferenceGenerator<T> {
		@Override
		public Difference<T> diff(T modified, T reference) {
			return new Difference<>(reference, modified, createDiffer().compare(modified, reference));
		}

		/**
		 * Creates a differ.
		 * <p> The differ must be instantiated each time we need it, as it's not thread safe.
		 * <p> Moreover, it looks like it has an internal state which might not be cleaned up in case of errors.
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
