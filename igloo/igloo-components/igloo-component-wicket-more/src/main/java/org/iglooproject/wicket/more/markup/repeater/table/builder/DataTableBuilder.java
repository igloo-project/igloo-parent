package org.iglooproject.wicket.more.markup.repeater.table.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.TargetBlankBehavior;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.AbstractDecoratingParameterizedComponentFactory;
import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.ISequenceProvider;
import igloo.wicket.model.ReadOnlyModel;
import igloo.wicket.model.SequenceProviders;
import igloo.wicket.renderer.Renderer;
import igloo.wicket.util.IDatePattern;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.commons.util.time.IDateTimePattern;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.BindingOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.FunctionOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.factory.ComponentFactories;
import org.iglooproject.wicket.more.markup.html.sort.ISortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.markup.repeater.table.BootstrapCardCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AjaxPagerAddInComponentFactory;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.CountAddInComponentFactory;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.LabelAddInComponentFactory;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.PagerAddInComponentFactory;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.ActionColumnBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.IActionColumnBaseBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnNoParameterBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.rows.DataTableRowsBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.rows.state.IDataTableRowsState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedBooleanLabelColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedBootstrapBadgeColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedLabelColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.table.DataTableTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.table.state.IDataTableTableState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.CustomizableToolbarBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreActionColumn;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreBooleanLabelColumn;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreBootstrapBadgeColumn;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreLabelColumn;
import org.iglooproject.wicket.more.markup.repeater.table.column.ICoreColumn;
import org.iglooproject.wicket.more.markup.repeater.table.toolbar.CoreHeadersToolbar;
import org.iglooproject.wicket.more.markup.repeater.table.toolbar.CoreNoRecordsToolbar;

public final class DataTableBuilder<T, S extends ISort<?>> implements IColumnState<T, S> {

  private final ISequenceProvider<T> sequenceProvider;

  private final CompositeSortModel<S> sortModel;

  private final Map<IColumn<T, S>, Condition> columns = Maps.newLinkedHashMap();

  private String noRecordsResourceKey;

  private final List<CustomizableToolbarBuilder<T, S>> topToolbarBuilders = Lists.newArrayList();

  private final List<CustomizableToolbarBuilder<T, S>> bottomToolbarBuilders = Lists.newArrayList();

  private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
      rowsBehaviorFactories = Lists.newArrayList();

  private final List<Behavior> tableBehaviors = Lists.newArrayList();

  private boolean showTopToolbar = true;

  private boolean showBottomToolbar = true;

  private IDataTableFactory<T, S> factory =
      new IDataTableFactory<T, S>() {
        private static final long serialVersionUID = 1L;

        @Override
        public CoreDataTable<T, S> create(
            String id,
            Map<IColumn<T, S>, Condition> columns,
            ISequenceProvider<T> sequenceProvider,
            List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
                rowsBehaviorFactories,
            List<Behavior> tableBehaviors,
            long rowsPerPage) {
          return new CoreDataTable<>(
              id, columns, sequenceProvider, rowsBehaviorFactories, tableBehaviors, rowsPerPage);
        }
      };

  private DataTableBuilder(ISequenceProvider<T> sequenceProvider, CompositeSortModel<S> sortModel) {
    super();
    this.sequenceProvider = sequenceProvider;
    this.sortModel = sortModel;
  }

  public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(
      IDataProvider<T> dataProvider) {
    return start(SequenceProviders.forDataProvider(dataProvider));
  }

  public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(
      IDataProvider<T> dataProvider, CompositeSortModel<S> sortModel) {
    return start(SequenceProviders.forDataProvider(dataProvider), sortModel);
  }

  public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(
      ISequenceProvider<T> sequenceProvider) {
    return new DataTableBuilder<>(
        sequenceProvider, new CompositeSortModel<S>(CompositingStrategy.LAST_ONLY));
  }

  public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(
      ISequenceProvider<T> sequenceProvider, CompositeSortModel<S> sortModel) {
    return new DataTableBuilder<>(sequenceProvider, sortModel);
  }

  @Override
  public IAddedColumnState<T, S> addColumn(final IColumn<T, S> column) {
    columns.put(column, null);
    return new AddedColumnState<IAddedColumnState<T, S>>() {
      @Override
      protected IColumn<T, S> getColumn() {
        return column;
      }

      @Override
      protected IAddedColumnState<T, S> getNextState() {
        return this;
      }
    };
  }

  @Override
  public IAddedCoreColumnState<T, S> addColumn(final ICoreColumn<T, S> column) {
    columns.put(column, null);
    return new AddedCoreColumnState<IAddedCoreColumnState<T, S>>() {
      @Override
      protected ICoreColumn<T, S> getColumn() {
        return column;
      }

      @Override
      protected IAddedCoreColumnState<T, S> getNextState() {
        return this;
      }
    };
  }

  protected IAddedLabelColumnState<T, S> addLabelColumn(CoreLabelColumn<T, S> column) {
    columns.put(column, null);
    return new AddedLabelColumnState(column);
  }

  @Override
  public IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel) {
    return addLabelColumn(new SimpleLabelColumn<T, S>(headerModel));
  }

  private static class SimpleLabelColumn<T, S extends ISort<?>> extends CoreLabelColumn<T, S> {
    private static final long serialVersionUID = 1L;

    public SimpleLabelColumn(IModel<String> headerLabelModel) {
      super(headerLabelModel);
    }

    @Override
    protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
      return new CoreLabel(componentId, rowModel);
    }
  }

  @Override
  public IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel, final Renderer<? super T> renderer) {
    return addLabelColumn(new RendererLabelColumn<T, S>(headerModel, renderer));
  }

  private static class RendererLabelColumn<T, S extends ISort<?>> extends CoreLabelColumn<T, S> {
    private static final long serialVersionUID = 1L;
    private final Renderer<? super T> renderer;

    public RendererLabelColumn(IModel<String> displayModel, Renderer<? super T> renderer) {
      super(displayModel);
      this.renderer = renderer;
    }

    @Override
    protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
      return new CoreLabel(componentId, renderer.asModel(rowModel));
    }
  }

  @Override
  public <C> IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel, final SerializableFunction2<? super T, C> function) {
    return addLabelColumn(new FunctionLabelColumn<T, S, C>(headerModel, function));
  }

  private static class FunctionLabelColumn<T, S extends ISort<?>, C> extends CoreLabelColumn<T, S> {
    private static final long serialVersionUID = 1L;
    private final SerializableFunction2<? super T, C> function;

    public FunctionLabelColumn(
        IModel<String> displayModel, SerializableFunction2<? super T, C> function) {
      super(displayModel);
      this.function = function;
    }

    @Override
    protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
      return new CoreLabel(componentId, ReadOnlyModel.of(rowModel, function));
    }
  }

  @Override
  public <C> IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel,
      final SerializableFunction2<? super T, C> function,
      final Renderer<? super C> renderer) {
    return addLabelColumn(
        new FunctionRendererLabelColumn<T, S, C>(headerModel, function, renderer));
  }

  private static class FunctionRendererLabelColumn<T, S extends ISort<?>, C>
      extends CoreLabelColumn<T, S> {
    private static final long serialVersionUID = 1L;
    private final SerializableFunction2<? super T, C> function;
    private final Renderer<? super C> renderer;

    public FunctionRendererLabelColumn(
        IModel<String> displayModel,
        SerializableFunction2<? super T, C> function,
        Renderer<? super C> renderer) {
      super(displayModel);
      this.function = function;
      this.renderer = renderer;
    }

    @Override
    protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
      return new CoreLabel(componentId, renderer.asModel(ReadOnlyModel.of(rowModel, function)));
    }
  }

  @Override
  public <C> IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel, final ICoreBinding<? super T, C> binding) {
    return addLabelColumn(new BindingLabelColumn<T, S, C>(headerModel, binding));
  }

  private static class BindingLabelColumn<T, S extends ISort<?>, C> extends CoreLabelColumn<T, S> {
    private static final long serialVersionUID = 1L;
    private final ICoreBinding<? super T, C> binding;

    public BindingLabelColumn(IModel<String> displayModel, ICoreBinding<? super T, C> binding) {
      super(displayModel);
      this.binding = binding;
    }

    @Override
    protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
      return new CoreLabel(componentId, BindingModel.of(rowModel, binding));
    }
  }

  @Override
  public <C> IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel,
      final ICoreBinding<? super T, C> binding,
      final Renderer<? super C> renderer) {
    return addLabelColumn(new BindingRendererLabelColumn<T, S, C>(headerModel, binding, renderer));
  }

  private static class BindingRendererLabelColumn<T, S extends ISort<?>, C>
      extends CoreLabelColumn<T, S> {
    private static final long serialVersionUID = 1L;
    private final ICoreBinding<? super T, C> binding;
    private final Renderer<? super C> renderer;

    public BindingRendererLabelColumn(
        IModel<String> displayModel,
        ICoreBinding<? super T, C> binding,
        Renderer<? super C> renderer) {
      super(displayModel);
      this.binding = binding;
      this.renderer = renderer;
    }

    @Override
    protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
      return new CoreLabel(componentId, renderer.asModel(BindingModel.of(rowModel, binding)));
    }
  }

  @Override
  public IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel,
      ICoreBinding<? super T, ? extends Date> binding,
      IDatePattern datePattern) {
    return addLabelColumn(headerModel, binding, Renderer.fromDatePattern(datePattern));
  }

  @Override
  public IAddedLabelColumnState<T, S> addLabelColumn(
      IModel<String> headerModel,
      ICoreBinding<? super T, ? extends TemporalAccessor> binding,
      IDateTimePattern dateTimePattern) {
    return addLabelColumn(headerModel, binding, Renderer.fromDateTimePattern(dateTimePattern));
  }

  @Override
  public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(
      IModel<String> headerModel,
      final ICoreBinding<? super T, C> binding,
      final IBootstrapRenderer<? super C> renderer) {
    CoreBootstrapBadgeColumn<T, S, C> column =
        new CoreBootstrapBadgeColumn<>(headerModel, binding, renderer);
    columns.put(column, null);
    return new AddedBootstrapBadgeColumnState<>(column);
  }

  @Override
  public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(
      IModel<String> headerModel,
      SerializableFunction2<? super T, C> function,
      IBootstrapRenderer<? super C> renderer) {
    CoreBootstrapBadgeColumn<T, S, C> column =
        new CoreBootstrapBadgeColumn<>(headerModel, function, renderer);
    columns.put(column, null);
    return new AddedBootstrapBadgeColumnState<>(column);
  }

  @Override
  public IAddedBooleanLabelColumnState<T, S> addBooleanLabelColumn(
      IModel<String> headerModel, final ICoreBinding<? super T, Boolean> binding) {
    CoreBooleanLabelColumn<T, S> column = new CoreBooleanLabelColumn<>(headerModel, binding);
    columns.put(column, null);
    return new AddedBooleanLabelColumnState(column);
  }

  @Override
  public CustomizableToolbarBuilder<T, S> addTopToolbar() {
    CustomizableToolbarBuilder<T, S> builder = new CustomizableToolbarBuilder<>(this);
    topToolbarBuilders.add(builder);
    return builder;
  }

  @Override
  public CustomizableToolbarBuilder<T, S> addBottomToolbar() {
    CustomizableToolbarBuilder<T, S> builder = new CustomizableToolbarBuilder<>(this);
    bottomToolbarBuilders.add(builder);
    return builder;
  }

  @Override
  public ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn() {
    return addActionColumn(Model.of(""));
  }

  @Override
  public ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn(
      final IModel<String> headerLabelModel) {
    return new ActionColumnBuilder<T, IAddedCoreColumnState<T, S>>() {
      @Override
      protected IAddedCoreColumnState<T, S> onEnd(List<IActionColumnBaseBuilder<T>> builders) {
        return addActionColumn(new CoreActionColumn<T, S>(headerLabelModel, builders));
      }
    };
  }

  private IAddedCoreColumnState<T, S> addActionColumn(final CoreActionColumn<T, S> column) {
    columns.put(column, null);
    return new AddedCoreColumnState<IAddedCoreColumnState<T, S>>() {
      @Override
      protected ICoreColumn<T, S> getColumn() {
        return column;
      }

      @Override
      protected IAddedCoreColumnState<T, S> getNextState() {
        return this;
      }
    };
  }

  @Override
  public IDataTableRowsState<T, S> rows() {
    return new DataTableRowsBuilder<T, S>() {
      @Override
      protected IBuildState<T, S> onEnd(
          List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
              rowsBehaviorFactories) {
        DataTableBuilder.this.rowsBehaviorFactories.addAll(rowsBehaviorFactories);
        return DataTableBuilder.this;
      }
    };
  }

  @Override
  public IDataTableTableState<T, S> table() {
    return new DataTableTableBuilder<T, S>() {
      @Override
      protected IBuildState<T, S> onEnd(List<Behavior> tableBehaviors) {
        DataTableBuilder.this.tableBehaviors.addAll(tableBehaviors);
        return DataTableBuilder.this;
      }
    };
  }

  @Override
  public DataTableBuilder<T, S> withNoRecordsResourceKey(String noRecordsResourceKey) {
    this.noRecordsResourceKey = noRecordsResourceKey;
    return this;
  }

  @Override
  public DataTableBuilder<T, S> hideHeadersToolbar() {
    showTopToolbar = false;
    return this;
  }

  @Override
  public DataTableBuilder<T, S> hideNoRecordsToolbar() {
    showBottomToolbar = false;
    return this;
  }

  @Override
  public IBuildState<T, S> withFactory(IDataTableFactory<T, S> factory) {
    this.factory = factory;
    return this;
  }

  @Override
  public CoreDataTable<T, S> build(String id) {
    return build(id, Long.MAX_VALUE);
  }

  @Override
  public CoreDataTable<T, S> build(String id, long rowsPerPage) {
    CoreDataTable<T, S> dataTable =
        factory.create(
            id, columns, sequenceProvider, rowsBehaviorFactories, tableBehaviors, rowsPerPage);
    finalizeBuild(dataTable);
    return dataTable;
  }

  protected void finalizeBuild(CoreDataTable<T, S> dataTable) {
    if (showTopToolbar) {
      for (CustomizableToolbarBuilder<T, S> builder : topToolbarBuilders) {
        dataTable.addTopToolbar(builder.build(dataTable));
      }
      dataTable.addTopToolbar(new CoreHeadersToolbar<S>(dataTable, sortModel));
    }
    if (showBottomToolbar) {
      dataTable.addBodyBottomToolbar(
          new CoreNoRecordsToolbar(
              dataTable,
              new ResourceModel(
                  noRecordsResourceKey != null ? noRecordsResourceKey : "common.list.empty")));
      for (CustomizableToolbarBuilder<T, S> builder : bottomToolbarBuilders) {
        dataTable.addBottomToolbar(builder.build(dataTable));
      }
    }
  }

  @Override
  public IDecoratedBuildState<T, S> decorate() {
    return new DecoratedBuildState();
  }

  @Override
  public IDecoratedBuildState<T, S> bootstrapCard() {
    return new BootstrapCardBuildState();
  }

  private abstract class DataTableBuilderWrapper implements IColumnState<T, S> {

    @Override
    public IAddedColumnState<T, S> addColumn(IColumn<T, S> column) {
      return DataTableBuilder.this.addColumn(column);
    }

    @Override
    public IAddedCoreColumnState<T, S> addColumn(ICoreColumn<T, S> column) {
      return DataTableBuilder.this.addColumn(column);
    }

    @Override
    public IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel) {
      return DataTableBuilder.this.addLabelColumn(headerModel);
    }

    @Override
    public IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel, Renderer<? super T> renderer) {
      return DataTableBuilder.this.addLabelColumn(headerModel, renderer);
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel, ICoreBinding<? super T, C> binding) {
      return DataTableBuilder.this.addLabelColumn(headerModel, binding);
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel,
        ICoreBinding<? super T, C> binding,
        Renderer<? super C> renderer) {
      return DataTableBuilder.this.addLabelColumn(headerModel, binding, renderer);
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel, SerializableFunction2<? super T, C> function) {
      return DataTableBuilder.this.addLabelColumn(headerModel, function);
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel,
        SerializableFunction2<? super T, C> function,
        Renderer<? super C> renderer) {
      return DataTableBuilder.this.addLabelColumn(headerModel, function, renderer);
    }

    @Override
    public IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel,
        ICoreBinding<? super T, ? extends Date> binding,
        IDatePattern datePattern) {
      return DataTableBuilder.this.addLabelColumn(headerModel, binding, datePattern);
    }

    @Override
    public IAddedLabelColumnState<T, S> addLabelColumn(
        IModel<String> headerModel,
        ICoreBinding<? super T, ? extends TemporalAccessor> binding,
        IDateTimePattern dateTimePattern) {
      return DataTableBuilder.this.addLabelColumn(headerModel, binding, dateTimePattern);
    }

    @Override
    public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(
        IModel<String> headerModel,
        ICoreBinding<? super T, C> binding,
        IBootstrapRenderer<? super C> renderer) {
      return DataTableBuilder.this.addBootstrapBadgeColumn(headerModel, binding, renderer);
    }

    @Override
    public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(
        IModel<String> headerModel,
        SerializableFunction2<? super T, C> function,
        IBootstrapRenderer<? super C> renderer) {
      return DataTableBuilder.this.addBootstrapBadgeColumn(headerModel, function, renderer);
    }

    @Override
    public IAddedBooleanLabelColumnState<T, S> addBooleanLabelColumn(
        IModel<String> headerModel, final ICoreBinding<? super T, Boolean> binding) {
      return DataTableBuilder.this.addBooleanLabelColumn(headerModel, binding);
    }

    @Override
    public CustomizableToolbarBuilder<T, S> addTopToolbar() {
      return DataTableBuilder.this.addTopToolbar();
    }

    @Override
    public CustomizableToolbarBuilder<T, S> addBottomToolbar() {
      return DataTableBuilder.this.addBottomToolbar();
    }

    @Override
    public ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn() {
      return DataTableBuilder.this.addActionColumn();
    }

    @Override
    public ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn(
        IModel<String> headerLabelModel) {
      return DataTableBuilder.this.addActionColumn(headerLabelModel);
    }

    @Override
    public IDataTableRowsState<T, S> rows() {
      return DataTableBuilder.this.rows();
    }

    @Override
    public IDataTableTableState<T, S> table() {
      return DataTableBuilder.this.table();
    }

    @Override
    public IBuildState<T, S> withNoRecordsResourceKey(String noRecordsResourceKey) {
      return DataTableBuilder.this.withNoRecordsResourceKey(noRecordsResourceKey);
    }

    @Override
    public IBuildState<T, S> hideHeadersToolbar() {
      return DataTableBuilder.this.hideHeadersToolbar();
    }

    @Override
    public IBuildState<T, S> hideNoRecordsToolbar() {
      return DataTableBuilder.this.hideNoRecordsToolbar();
    }

    @Override
    public IBuildState<T, S> withFactory(IDataTableFactory<T, S> factory) {
      return DataTableBuilder.this.withFactory(factory);
    }

    @Override
    public CoreDataTable<T, S> build(String id) {
      return DataTableBuilder.this.build(id);
    }

    @Override
    public CoreDataTable<T, S> build(String id, long rowsPerPage) {
      return DataTableBuilder.this.build(id, rowsPerPage);
    }

    @Override
    public IDecoratedBuildState<T, S> decorate() {
      return DataTableBuilder.this.decorate();
    }

    @Override
    public IDecoratedBuildState<T, S> bootstrapCard() {
      return DataTableBuilder.this.bootstrapCard();
    }
  }

  private abstract class AddedColumnState<NextState extends IAddedColumnState<T, S>>
      extends DataTableBuilderWrapper implements IAddedColumnState<T, S> {

    protected abstract IColumn<T, S> getColumn();

    protected abstract NextState getNextState();

    @Override
    public NextState when(Condition condition) {
      columns.put(getColumn(), condition);
      return getNextState();
    }
  }

  private abstract class AddedCoreColumnState<NextState extends IAddedCoreColumnState<T, S>>
      extends AddedColumnState<NextState> implements IAddedCoreColumnState<T, S> {

    @Override
    protected abstract ICoreColumn<T, S> getColumn();

    @Override
    public NextState withClass(String cssClass) {
      getColumn().addCssClass(cssClass);
      return getNextState();
    }

    @Override
    public NextState withSort(S sort) {
      return withSort(sort, SortIconStyle.DEFAULT, CycleMode.NONE_DEFAULT);
    }

    @Override
    public NextState withSort(S sort, ISortIconStyle sortIconStyle) {
      return withSort(sort, sortIconStyle, CycleMode.NONE_DEFAULT);
    }

    @Override
    public NextState withSort(S sort, ISortIconStyle sortIconStyle, CycleMode cycleMode) {
      ICoreColumn<T, S> column = getColumn();
      column.setSortProperty(sort);
      column.setSortCycleMode(cycleMode);
      column.setSortIconStyle(sortIconStyle);
      return getNextState();
    }
  }

  private class AddedLabelColumnState extends AddedCoreColumnState<IAddedLabelColumnState<T, S>>
      implements IAddedLabelColumnState<T, S> {

    private final CoreLabelColumn<T, S> column;

    public AddedLabelColumnState(CoreLabelColumn<T, S> column) {
      super();
      this.column = column;
    }

    @Override
    protected CoreLabelColumn<T, S> getColumn() {
      return column;
    }

    @Override
    public IAddedLabelColumnState<T, S> getNextState() {
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> multiline() {
      getColumn().multiline();
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> showPlaceholder() {
      getColumn().showPlaceholder();
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> showPlaceholder(IModel<String> placeholderModel) {
      getColumn().showPlaceholder(placeholderModel);
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> withTooltip(Renderer<? super T> tooltipRenderer) {
      getColumn().setTooltipRenderer(tooltipRenderer);
      return this;
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> withTooltip(
        SerializableFunction2<? super T, C> function, Renderer<? super C> tooltipRenderer) {
      return withTooltip(tooltipRenderer.onResultOf(function));
    }

    @Override
    public IAddedLabelColumnState<T, S> withLink(
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper) {
      getColumn().setLinkGeneratorMapper(linkGeneratorMapper);
      return this;
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> withLink(
        SerializableFunction2<? super T, C> function,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
      return withLink(
          new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> withLink(
        ICoreBinding<? super T, C> binding,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
      return withLink(new BindingOneParameterLinkDescriptorMapper<>(binding, linkGeneratorMapper));
    }

    @Override
    public IAddedLabelColumnState<T, S> withSideLink(
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper) {
      getColumn().setSideLinkGeneratorMapper(linkGeneratorMapper);
      return this;
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> withSideLink(
        SerializableFunction2<? super T, C> function,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
      return withSideLink(
          new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
    }

    @Override
    public <C> IAddedLabelColumnState<T, S> withSideLink(
        ICoreBinding<? super T, C> binding,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
      return withSideLink(
          new BindingOneParameterLinkDescriptorMapper<>(binding, linkGeneratorMapper));
    }

    @Override
    @Deprecated
    public IAddedLabelColumnState<T, S> disableIfInvalid() {
      // Does nothing: this is the default behavior.
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> hideIfInvalid() {
      getColumn().hideIfInvalid();
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> linkBehavior(Behavior linkBehavior) {
      getColumn().addLinkBehavior(linkBehavior);
      return this;
    }

    @Override
    public IAddedLabelColumnState<T, S> targetBlank() {
      return linkBehavior(new TargetBlankBehavior());
    }
  }

  private class AddedBootstrapBadgeColumnState<C>
      extends AddedCoreColumnState<IAddedBootstrapBadgeColumnState<T, S, C>>
      implements IAddedBootstrapBadgeColumnState<T, S, C> {

    private final CoreBootstrapBadgeColumn<T, S, C> column;

    public AddedBootstrapBadgeColumnState(CoreBootstrapBadgeColumn<T, S, C> column) {
      super();
      this.column = column;
    }

    @Override
    protected CoreBootstrapBadgeColumn<T, S, C> getColumn() {
      return column;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> getNextState() {
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> badgePill() {
      return badgePill(Condition.alwaysTrue());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> badgePill(Condition badgePill) {
      getColumn().badgePill(badgePill);
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> hideIcon() {
      return showIcon(Condition.alwaysFalse());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> showIcon() {
      return showIcon(Condition.alwaysTrue());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> showIcon(Condition showIcon) {
      getColumn().showIcon(showIcon);
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> hideLabel() {
      return showLabel(Condition.alwaysFalse());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> showLabel() {
      return showLabel(Condition.alwaysTrue());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> showLabel(Condition showLabel) {
      getColumn().showLabel(showLabel);
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> hideTooltip() {
      return showTooltip(Condition.alwaysFalse());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> showTooltip() {
      return showTooltip(Condition.alwaysTrue());
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> showTooltip(Condition showTooltip) {
      getColumn().showTooltip(showTooltip);
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> withLink(
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper) {
      getColumn().setLinkGeneratorMapper(linkGeneratorMapper);
      return this;
    }

    @Override
    public <E> IAddedBootstrapBadgeColumnState<T, S, C> withLink(
        SerializableFunction2<? super T, E> function,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
      return withLink(
          new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
    }

    @Override
    public <E> IAddedBootstrapBadgeColumnState<T, S, C> withLink(
        ICoreBinding<? super T, E> binding,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
      return withLink(new BindingOneParameterLinkDescriptorMapper<>(binding, linkGeneratorMapper));
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> withSideLink(
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper) {
      getColumn().setSideLinkGeneratorMapper(linkGeneratorMapper);
      return this;
    }

    @Override
    public <E> IAddedBootstrapBadgeColumnState<T, S, C> withSideLink(
        SerializableFunction2<? super T, E> function,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
      return withSideLink(
          new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
    }

    @Override
    public <E> IAddedBootstrapBadgeColumnState<T, S, C> withSideLink(
        ICoreBinding<? super T, E> binding,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
      return withSideLink(
          new BindingOneParameterLinkDescriptorMapper<>(binding, linkGeneratorMapper));
    }

    @Override
    @Deprecated
    public IAddedBootstrapBadgeColumnState<T, S, C> disableIfInvalid() {
      // Does nothing: this is the default behavior.
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> hideIfInvalid() {
      getColumn().hideIfInvalid();
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> throwExceptionIfInvalid() {
      getColumn().throwExceptionIfInvalid();
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> linkBehavior(Behavior linkBehavior) {
      getColumn().addLinkBehavior(linkBehavior);
      return this;
    }

    @Override
    public IAddedBootstrapBadgeColumnState<T, S, C> targetBlank() {
      return linkBehavior(new TargetBlankBehavior());
    }
  }

  private class AddedBooleanLabelColumnState
      extends AddedCoreColumnState<IAddedBooleanLabelColumnState<T, S>>
      implements IAddedBooleanLabelColumnState<T, S> {

    private final CoreBooleanLabelColumn<T, S> column;

    public AddedBooleanLabelColumnState(CoreBooleanLabelColumn<T, S> column) {
      super();
      this.column = column;
    }

    @Override
    protected CoreBooleanLabelColumn<T, S> getColumn() {
      return column;
    }

    @Override
    public IAddedBooleanLabelColumnState<T, S> hideIfNullOrFalse() {
      getColumn().hideIfNullOrFalse();
      return this;
    }

    @Override
    protected IAddedBooleanLabelColumnState<T, S> getNextState() {
      return this;
    }
  }

  private class DecoratedBuildState implements IDecoratedBuildState<T, S> {
    private static final int DEFAULT_PAGER_VIEW_SIZE = 7;

    protected String countResourceKey = null;

    protected Condition responsiveCondition = Condition.alwaysTrue();

    protected final Multimap<
            AddInPlacement,
            IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>>
        addInComponentFactories = ArrayListMultimap.create();

    protected String getTitleCssClass() {
      return "add-in-emphasize";
    }

    protected String getPaginationCssClass() {
      return "add-in-pagination";
    }

    @Override
    public IDecoratedBuildState<T, S> responsive(Condition responsiveCondition) {
      this.responsiveCondition = responsiveCondition;
      return this;
    }

    @Override
    public IActionColumnNoParameterBuildState<Void, IDecoratedBuildState<T, S>> actions(
        final AddInPlacement placement) {
      return new ActionColumnBuilder<Void, IDecoratedBuildState<T, S>>() {
        @Override
        protected IDecoratedBuildState<T, S> onEnd(List<IActionColumnBaseBuilder<Void>> builders) {
          for (final IOneParameterComponentFactory<?, IModel<Void>> builder : builders) {
            addIn(placement, wicketId -> builder.create(wicketId, null));
          }
          return DecoratedBuildState.this;
        }
      };
    }

    @Override
    public <Z> IActionColumnBuildState<Z, IDecoratedBuildState<T, S>> actions(
        final AddInPlacement placement, final IModel<Z> model) {
      return new ActionColumnBuilder<Z, IDecoratedBuildState<T, S>>() {
        @Override
        protected IDecoratedBuildState<T, S> onEnd(List<IActionColumnBaseBuilder<Z>> builders) {
          for (final IOneParameterComponentFactory<?, IModel<Z>> builder : builders) {
            addIn(placement, wicketId -> builder.create(wicketId, model));
          }
          return DecoratedBuildState.this;
        }
      };
    }

    @Override
    public IDecoratedBuildState<T, S> title(String resourceKey) {
      return addIn(
          AddInPlacement.HEADING_LEFT,
          new LabelAddInComponentFactory(new ResourceModel(resourceKey)),
          getTitleCssClass());
    }

    @Override
    public IDecoratedBuildState<T, S> title(IModel<?> model) {
      return addIn(
          AddInPlacement.HEADING_LEFT, new LabelAddInComponentFactory(model), getTitleCssClass());
    }

    @Override
    public IDecoratedBuildState<T, S> title(IComponentFactory<?> addInComponentFactory) {
      return addIn(AddInPlacement.HEADING_LEFT, addInComponentFactory, getTitleCssClass());
    }

    @Override
    public IDecoratedBuildState<T, S> count(String countResourceKey) {
      return count(AddInPlacement.HEADING_LEFT, countResourceKey);
    }

    @Override
    public IDecoratedBuildState<T, S> count(AddInPlacement placement, String countResourceKey) {
      this.countResourceKey = countResourceKey;
      return addIn(
          placement,
          new CountAddInComponentFactory(sequenceProvider, countResourceKey),
          getTitleCssClass());
    }

    @Override
    public IDecoratedBuildState<T, S> pagers() {
      return pagers(DEFAULT_PAGER_VIEW_SIZE);
    }

    @Override
    public IDecoratedBuildState<T, S> pagers(int viewSize) {
      return pager(AddInPlacement.HEADING_RIGHT, viewSize)
          .pager(AddInPlacement.FOOTER_RIGHT, viewSize);
    }

    @Override
    public IDecoratedBuildState<T, S> pager(AddInPlacement placement) {
      return pager(placement, DEFAULT_PAGER_VIEW_SIZE);
    }

    @Override
    public IDecoratedBuildState<T, S> pager(AddInPlacement placement, int viewSize) {
      return addIn(placement, new PagerAddInComponentFactory(viewSize), getPaginationCssClass());
    }

    @Override
    public IDecoratedBuildState<T, S> ajaxPagers() {
      return ajaxPagers(DEFAULT_PAGER_VIEW_SIZE);
    }

    @Override
    public IDecoratedBuildState<T, S> ajaxPagers(int viewSize) {
      return ajaxPager(AddInPlacement.HEADING_RIGHT, viewSize)
          .ajaxPager(AddInPlacement.FOOTER_RIGHT, viewSize);
    }

    @Override
    public IDecoratedBuildState<T, S> ajaxPager(AddInPlacement placement) {
      return ajaxPager(placement, DEFAULT_PAGER_VIEW_SIZE);
    }

    @Override
    public IDecoratedBuildState<T, S> ajaxPager(AddInPlacement placement, int viewSize) {
      return addIn(
          placement, new AjaxPagerAddInComponentFactory(viewSize), getPaginationCssClass());
    }

    @Override
    public IDecoratedBuildState<T, S> addIn(
        AddInPlacement placement, IComponentFactory<?> addInComponentFactory) {
      return addIn(placement, ComponentFactories.ignoreParameter(addInComponentFactory));
    }

    @Override
    public IDecoratedBuildState<T, S> addIn(
        AddInPlacement placement, IComponentFactory<?> addInComponentFactory, String cssClasses) {
      return addIn(
          placement, ComponentFactories.ignoreParameter(addInComponentFactory), cssClasses);
    }

    @Override
    public IDecoratedBuildState<T, S> addIn(
        AddInPlacement placement,
        IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>
            addInComponentFactory) {
      addInComponentFactories.put(placement, addInComponentFactory);
      return this;
    }

    @Override
    public IDecoratedBuildState<T, S> addIn(
        AddInPlacement placement,
        IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>
            addInComponentFactory,
        String cssClasses) {
      return addIn(
          placement,
          new ClassAttributeAppenderDecoratingParameterizedComponentFactory<>(
              addInComponentFactory, cssClasses));
    }

    @Override
    public DecoratedCoreDataTablePanel<T, S> build(String id) {
      return build(id, Long.MAX_VALUE);
    }

    @Override
    public DecoratedCoreDataTablePanel<T, S> build(String id, long rowsPerPage) {
      DecoratedCoreDataTablePanel<T, S> panel =
          new DecoratedCoreDataTablePanel<>(
              id,
              factory,
              columns,
              sequenceProvider,
              rowsBehaviorFactories,
              tableBehaviors,
              rowsPerPage,
              addInComponentFactories,
              responsiveCondition);

      if (noRecordsResourceKey == null && countResourceKey != null) {
        withNoRecordsResourceKey(countResourceKey + ".zero");
      }

      DataTableBuilder.this.finalizeBuild(panel.getDataTable());

      return panel;
    }
  }

  private class BootstrapCardBuildState extends DecoratedBuildState {

    @Override
    protected String getTitleCssClass() {
      return "add-in-title";
    }

    @Override
    protected String getPaginationCssClass() {
      return "add-in-pagination add-in-pagination-panel";
    }

    @Override
    public DecoratedCoreDataTablePanel<T, S> build(String id, long rowsPerPage) {
      BootstrapCardCoreDataTablePanel<T, S> panel =
          new BootstrapCardCoreDataTablePanel<>(
              id,
              factory,
              columns,
              sequenceProvider,
              rowsBehaviorFactories,
              tableBehaviors,
              rowsPerPage,
              addInComponentFactories,
              responsiveCondition);

      if (noRecordsResourceKey == null && countResourceKey != null) {
        withNoRecordsResourceKey(countResourceKey + ".zero");
      }

      DataTableBuilder.this.finalizeBuild(panel.getDataTable());

      return panel;
    }
  }

  private static class ClassAttributeAppenderDecoratingParameterizedComponentFactory<
          C extends Component, P>
      extends AbstractDecoratingParameterizedComponentFactory<C, P> {

    private static final long serialVersionUID = 4455974327179014829L;

    private final String cssClass;

    public ClassAttributeAppenderDecoratingParameterizedComponentFactory(
        IOneParameterComponentFactory<? extends C, ? super P> delegate, String cssClass) {
      super(delegate);
      this.cssClass = cssClass;
    }

    @Override
    protected C decorate(C component, P parameter) {
      component.add(new ClassAttributeAppender(cssClass));
      return component;
    }
  }
}
