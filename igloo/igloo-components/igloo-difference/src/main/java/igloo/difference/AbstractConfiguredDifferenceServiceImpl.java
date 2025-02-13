package igloo.difference;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.differ.BeanDiffer;
import de.danielbechler.diff.instantiation.TypeInfo;
import de.danielbechler.diff.introspection.Introspector;
import de.danielbechler.diff.introspection.StandardIntrospector;
import de.danielbechler.diff.introspection.TypeInfoResolver;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.Visitor;
import de.danielbechler.diff.node.Visit;
import igloo.difference.model.DifferenceField;
import igloo.difference.model.DifferenceFields;
import igloo.difference.model.DifferenceMode;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import org.hibernate.proxy.HibernateProxy;
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
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.service.IDifferenceService;
import org.iglooproject.jpa.more.business.difference.util.CompositeProxyInitializer;
import org.iglooproject.jpa.more.business.difference.util.DiffUtils;
import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IProxyInitializer;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractConfiguredDifferenceServiceImpl<T extends GenericEntity<?, ?>>
    implements IDifferenceService<T> {

  @Autowired private IEntityService entityService;

  @Autowired private IRendererService rendererService;

  @Autowired
  private ITransactionScopeIndependantRunnerService transactionScopeIndependantRunnerService;

  @Autowired
  @Qualifier("defaultHistoryDifferenceFactory")
  private DefaultHistoryDifferenceFactory<T> defaultHistoryDifferenceFactory;

  private IProxyInitializer<T> proxyInitializer;
  private Multimap<FieldPath, IHistoryDifferenceFactory<T>> specificHistoryDifferenceFactories;

  private IDifferenceFromReferenceGenerator<T> mainDifferenceGenerator;

  private final DifferenceConfigurer differenceConfigurer;

  private final Map<FieldPath, DifferenceField> differenceFieldsByFieldPath = new HashMap<>();

  protected AbstractConfiguredDifferenceServiceImpl(DifferenceFields fields) {
    this.differenceConfigurer = new DifferenceConfigurer(fields);
  }

  @SuppressWarnings("unchecked")
  @PostConstruct
  protected void initialize() {
    this.mainDifferenceGenerator =
        new AbstractDifferenceFromReferenceGenerator() {
          @Override
          protected ObjectDiffer createDiffer() {
            return initializeDiffer(DiffUtils.builder()).build();
          }
        };

    List<IProxyInitializer<? super T>> initializers = Lists.newArrayList();

    // Customized initializations
    Iterables.addAll(initializers, initializeInitializers());

    this.proxyInitializer = new CompositeProxyInitializer<>(initializers);

    // Customized creation of the HistoryDifference items
    ImmutableMultimap.Builder<FieldPath, IHistoryDifferenceFactory<T>> factoriesMapBuilder =
        ImmutableMultimap.builder();
    Multimap<IHistoryDifferenceFactory<T>, FieldPath>
        specificHistoryDifferenceFactoriesToFieldPaths = getSpecificHistoryDifferenceFactories();
    for (Entry<IHistoryDifferenceFactory<T>, FieldPath> entry :
        specificHistoryDifferenceFactoriesToFieldPaths.entries()) {
      factoriesMapBuilder.putAll(entry.getValue(), entry.getKey());
    }
    specificHistoryDifferenceFactories = factoriesMapBuilder.build();

    // register paths
    differenceFieldsByFieldPath.putAll(differenceConfigurer.getPaths());
  }

  protected Multimap<IHistoryDifferenceFactory<T>, FieldPath>
      getSpecificHistoryDifferenceFactories() {
    return ImmutableMultimap.<IHistoryDifferenceFactory<T>, FieldPath>of();
  }

  protected ObjectDifferBuilder initializeDiffer(final ObjectDifferBuilder builder) {
    // Ignore Hibernate proxies fields
    builder
        .introspection()
        .setDefaultIntrospector(
            new Introspector() {
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

    // TODO modify java-object-diff to allow to ignore useEqual by path to
    // ignore useEqual on root ?
    // As we possibly register a root type -> useEqualsMethod, we need to
    // ensure that root node is always compared
    // by introspection. OverrideRootComparisonStrategyResolver handles this
    // special case and delegates other cases
    // to original behavior.
    // This differ must be registered before collection differs so that it
    // is applied after collection differs.
    // (ObjectDiffBuilder behavior: custom differs tested first, last
    // registered first, so we intend this order:
    // (custom) MultimapDiffer, ExtendedCollectionDiffer, BeanDiffer with
    // comparisonStrategy resolver first
    // (java-object-diff) primitive, map, collection, BeanDiffer
    // This is working as BeanDiffer excludes primitive types
    builder
        .differs()
        .register(
            (differDispatcher, nodeQueryService) ->
                new BeanDiffer(
                    differDispatcher,
                    nodeQueryService,
                    nodeQueryService,
                    new OverrideRootComparisonStrategyResolver(nodeQueryService),
                    (TypeInfoResolver) builder.introspection()));

    builder
        .differs()
        .register(
            (differDispatcher, nodeQueryService) -> {
              ExtendedCollectionDiffer differ =
                  new ExtendedCollectionDiffer(
                      differDispatcher, nodeQueryService, nodeQueryService);
              return initializeCollectionDiffer(differ);
            });

    builder
        .differs()
        .register(
            (differDispatcher, nodeQueryService) -> {
              MultimapDiffer differ =
                  new MultimapDiffer(differDispatcher, nodeQueryService, nodeQueryService);
              return initializeMultimapDiffer(differ);
            });

    differenceConfigurer.configureDiffer(builder);

    return builder;
  }

  @SuppressWarnings("unchecked")
  protected Iterable<? extends IProxyInitializer<? super T>> initializeInitializers() {
    return (Iterable<? extends IProxyInitializer<? super T>>)
        differenceConfigurer.initializeInitializers();
  }

  protected ExtendedCollectionDiffer initializeCollectionDiffer(ExtendedCollectionDiffer differ) {
    differenceConfigurer.initializeCollectionDiffer(differ);
    return differ;
  }

  protected MultimapDiffer initializeMultimapDiffer(MultimapDiffer differ) {
    return differ;
  }

  @Override
  public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> toHistoryDifferences(
      final Supplier2<HD> historyDifferenceSupplier, final Difference<T> rootDifference) {
    final Multimap<IHistoryDifferenceFactory<T>, DiffNode> factoriesToNodes =
        LinkedHashMultimap.create();

    // Lists the leaf nodes and attributes the nodes to specific factories
    // if needed.
    // A "leaf" is either a node without children or a node which is an
    // element of a Collection or a Map.
    rootDifference
        .getDiffNode()
        .visitChildren(
            new Visitor() {
              private Deque<FieldPathComponent> pathComponents = new LinkedList<>();

              @Override
              public void node(DiffNode node, Visit visit) {
                visit.dontGoDeeper();
                FieldPathComponent component =
                    DiffUtils.toFieldPathComponent(node.getPath().getLastElementSelector());
                pathComponents.addLast(component);
                FieldPath fieldPath = FieldPath.of(pathComponents);
                Collection<IHistoryDifferenceFactory<T>> specificFactories =
                    specificHistoryDifferenceFactories.get(fieldPath);
                if (!specificFactories.isEmpty()) {
                  for (IHistoryDifferenceFactory<T> specificFactory : specificFactories) {
                    factoriesToNodes.put(specificFactory, node);
                  }
                } else {
                  boolean createHistoryDifference = true;
                  if (differenceFieldsByFieldPath.containsKey(fieldPath)) {
                    DifferenceField field = differenceFieldsByFieldPath.get(fieldPath);
                    createHistoryDifference =
                        !field.isContainer() && !field.getMode().equals(DifferenceMode.DEEP);
                  }
                  if (component == FieldPathComponent.ITEM || createHistoryDifference) {
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
      for (Map.Entry<IHistoryDifferenceFactory<T>, Collection<DiffNode>> entry :
          factoriesToNodes.asMap().entrySet()) {
        IHistoryDifferenceFactory<T> factory = entry.getKey();
        Collection<DiffNode> nodes = entry.getValue();
        historyDifferences.addAll(
            factory.create(
                historyDifferenceSupplier,
                rootDifference,
                nodes,
                this::needHistoryDifferenceCreation));
      }
    } catch (RuntimeException e) {
      throw new IllegalStateException("Unexpected exception while computing HistoryDifferences", e);
    }
    return historyDifferences;
  }

  /**
   * This method is passed as lambda to {@link DefaultHistoryDifferenceFactory}. It allows to use
   * field metadata from {@link #differenceFieldsByFieldPath} to prevent History Difference creation
   * for deep field without child diff.
   *
   * <p>This is needed for Embeddable that switch from null to object without any embedded values),
   * as null -> Embeddable is a useless information if there is no child difference.
   */
  public boolean needHistoryDifferenceCreation(DiffNode diffNode) {
    String path = PathHelper.normalizeNodePath(diffNode.getPath());
    FieldPath fieldPath = FieldPath.fromString(path);
    if (differenceFieldsByFieldPath.containsKey(fieldPath)) {
      DifferenceField field = differenceFieldsByFieldPath.get(fieldPath);
      return !field.isContainer() && !field.getMode().equals(DifferenceMode.DEEP);
    }
    // default behavior
    return true;
  }

  private abstract class AbstractDifferenceFromReferenceGenerator
      implements IDifferenceFromReferenceGenerator<T> {
    @Override
    public Difference<T> diff(T modified, T reference) {
      return new Difference<>(reference, modified, createDiffer().compare(modified, reference));
    }

    /**
     * Creates a differ.
     *
     * <p>The differ must be instantiated each time we need it, as it's not thread safe.
     *
     * <p>Moreover, it looks like it has an internal state which might not be cleaned up in case of
     * errors.
     */
    protected abstract ObjectDiffer createDiffer();

    @Override
    public Difference<T> diffFromReference(T after) {
      T before =
          transactionScopeIndependantRunnerService.run(getReferenceProviderAndInitializer(after));
      return diff(after, before);
    }

    private Callable<T> getReferenceProviderAndInitializer(T value) {
      final GenericEntityReference<?, T> reference = GenericEntityReference.ofUnknownIdType(value);
      return () -> {
        if (reference == null) {
          return null;
        } else {
          T databaseVersion = HibernateUtils.unwrap(entityService.getEntity(reference));
          proxyInitializer.initialize(databaseVersion);
          return databaseVersion;
        }
      };
    }

    @Override
    public Callable<T> getReferenceProvider(T value) {
      final GenericEntityReference<?, T> reference = GenericEntityReference.ofUnknownIdType(value);
      return () -> {
        if (reference == null) {
          return null;
        } else {
          T databaseVersion = entityService.getEntity(reference);
          return HibernateUtils.unwrap(databaseVersion);
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
}
