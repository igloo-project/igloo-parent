package fr.openwide.core.wicket.more.markup.repeater.table.builder;

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

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.factory.LinkGeneratorFactory;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.BindingOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.FunctionOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter;
import fr.openwide.core.wicket.more.markup.html.basic.TargetBlankBehavior;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractDecoratingParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.ComponentFactories;
import fr.openwide.core.wicket.more.markup.html.factory.IComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.sort.ISortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.SortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import fr.openwide.core.wicket.more.markup.repeater.sequence.ISequenceProvider;
import fr.openwide.core.wicket.more.markup.repeater.table.BootstrapPanelCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.repeater.table.CoreDataTable;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AjaxPagerAddInComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.CountAddInComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.LabelAddInComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.PagerAddInComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.AbstractActionColumnElementBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.ActionColumnBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnBuildState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnNoParameterBuildState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedBooleanLabelColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedBootstrapBadgeColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedLabelColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IBuildState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.toolbar.CustomizableToolbarBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.column.CoreActionColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.column.CoreBooleanLabelColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.column.CoreBootstrapBadgeColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.column.CoreBootstrapLabelColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.column.CoreLabelColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.column.ICoreColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.toolbar.CoreHeadersToolbar;
import fr.openwide.core.wicket.more.markup.repeater.table.toolbar.CoreNoRecordsToolbar;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;
import fr.openwide.core.wicket.more.rendering.Renderer;
import fr.openwide.core.wicket.more.util.IDatePattern;
import fr.openwide.core.wicket.more.util.model.SequenceProviders;

public final class DataTableBuilder<T, S extends ISort<?>> implements IColumnState<T, S> {

	private final ISequenceProvider<T> sequenceProvider;

	private final CompositeSortModel<S> sortModel;

	private final Map<IColumn<T, S>, Condition> columns = Maps.newLinkedHashMap();

	private String noRecordsResourceKey;

	private final List<CustomizableToolbarBuilder<T, S>> topToolbarBuilders = Lists.newArrayList();

	private final List<CustomizableToolbarBuilder<T, S>> bottomToolbarBuilders = Lists.newArrayList();

	private boolean showTopToolbar = true;
	
	private boolean showBottomToolbar = true;

	private IDataTableFactory<T, S> factory = new IDataTableFactory<T, S>() {
		private static final long serialVersionUID = 1L;
		@Override
		public CoreDataTable<T, S> create(String id, Map<IColumn<T, S>, Condition> columns, ISequenceProvider<T> sequenceProvider, long rowsPerPage) {
			return new CoreDataTable<T, S>(id, columns, sequenceProvider, rowsPerPage);
		}
	};

	private DataTableBuilder(ISequenceProvider<T> sequenceProvider, CompositeSortModel<S> sortModel) {
		super();
		this.sequenceProvider = sequenceProvider;
		this.sortModel = sortModel;
	}

	public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(IDataProvider<T> dataProvider) {
		return start(SequenceProviders.forDataProvider(dataProvider));
	}

	public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(IDataProvider<T> dataProvider, CompositeSortModel<S> sortModel) {
		return start(SequenceProviders.forDataProvider(dataProvider), sortModel);
	}

	public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(ISequenceProvider<T> sequenceProvider) {
		return new DataTableBuilder<T, S>(sequenceProvider, new CompositeSortModel<S>(CompositingStrategy.LAST_ONLY));
	}

	public static <T, S extends ISort<?>> DataTableBuilder<T, S> start(ISequenceProvider<T> sequenceProvider, CompositeSortModel<S> sortModel) {
		return new DataTableBuilder<T, S>(sequenceProvider, sortModel);
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
	public IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, final Renderer<? super T> renderer) {
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
	public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, final Function<? super T, C> function) {
		return addLabelColumn(new FunctionLabelColumn<T, S, C>(headerModel, function));
	}
	
	private static class FunctionLabelColumn<T, S extends ISort<?>, C> extends CoreLabelColumn<T, S> {
		private static final long serialVersionUID = 1L;
		private final Function<? super T, C> function;
		public FunctionLabelColumn(IModel<String> displayModel, Function<? super T, C> function) {
			super(displayModel);
			this.function = function;
		}
		@Override
		protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
			return new CoreLabel(componentId, ReadOnlyModel.of(rowModel, function));
		}
	}

	@Override
	public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			final Function<? super T, C> function, final Renderer<? super C> renderer) {
		return addLabelColumn(new FunctionRendererLabelColumn<T, S, C>(headerModel, function, renderer));
	}
	
	private static class FunctionRendererLabelColumn<T, S extends ISort<?>, C> extends CoreLabelColumn<T, S> {
		private static final long serialVersionUID = 1L;
		private final Function<? super T, C> function;
		private final Renderer<? super C> renderer;
		public FunctionRendererLabelColumn(IModel<String> displayModel, Function<? super T, C> function, Renderer<? super C> renderer) {
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
	public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, final AbstractCoreBinding<? super T, C> binding) {
		return addLabelColumn(new BindingLabelColumn<T, S, C>(headerModel, binding));
	}
	
	private static class BindingLabelColumn<T, S extends ISort<?>, C> extends CoreLabelColumn<T, S> {
		private static final long serialVersionUID = 1L;
		private final AbstractCoreBinding<? super T, C> binding;
		public BindingLabelColumn(IModel<String> displayModel, AbstractCoreBinding<? super T, C> binding) {
			super(displayModel);
			this.binding = binding;
		}
		@Override
		protected CoreLabel newLabel(String componentId, IModel<T> rowModel) {
			return new CoreLabel(componentId, BindingModel.of(rowModel, binding));
		}
	}

	@Override
	public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			final AbstractCoreBinding<? super T, C> binding, final Renderer<? super C> renderer) {
		return addLabelColumn(new BindingRendererLabelColumn<T, S, C>(headerModel, binding, renderer));
	}
	
	private static class BindingRendererLabelColumn<T, S extends ISort<?>, C> extends CoreLabelColumn<T, S> {
		private static final long serialVersionUID = 1L;
		private final AbstractCoreBinding<? super T, C> binding;
		private final Renderer<? super C> renderer;
		public BindingRendererLabelColumn(IModel<String> displayModel, AbstractCoreBinding<? super T, C> binding, Renderer<? super C> renderer) {
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
	public IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel,
			AbstractCoreBinding<? super T, ? extends Date> binding, IDatePattern datePattern) {
		return addLabelColumn(headerModel, binding, Renderer.fromDatePattern(datePattern));
	}
	
	@Override
	public <C> IAddedCoreColumnState<T, S> addBootstrapLabelColumn(IModel<String> headerModel,
			final AbstractCoreBinding<? super T, C> binding, final BootstrapRenderer<? super C> renderer) {
		return addColumn(new CoreBootstrapLabelColumn<T, S, C>(headerModel, binding, renderer));
	}
	
	@Override
	public <C> IAddedCoreColumnState<T, S> addBootstrapLabelColumn(IModel<String> headerModel,
			Function<? super T, C> function, BootstrapRenderer<? super C> renderer) {
		return addColumn(new CoreBootstrapLabelColumn<T, S, C>(headerModel, function, renderer));
	}
	
	@Override
	public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
			final AbstractCoreBinding<? super T, C> binding, final BootstrapRenderer<? super C> renderer) {
		CoreBootstrapBadgeColumn<T, S, C> column = new CoreBootstrapBadgeColumn<T, S, C>(headerModel, binding, renderer);
		columns.put(column, null);
		return new AddedBootstrapBadgeColumnState<C>(column);
	}
	
	@Override
	public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
			Function<? super T, C> function, BootstrapRenderer<? super C> renderer) {
		CoreBootstrapBadgeColumn<T, S, C> column = new CoreBootstrapBadgeColumn<T, S, C>(headerModel, function, renderer);
		columns.put(column, null);
		return new AddedBootstrapBadgeColumnState<C>(column);
	}
	
	@Override
	public <C> IAddedBooleanLabelColumnState<T, S> addBooleanLabelColumn(IModel<String> headerModel,
			final AbstractCoreBinding<? super T, Boolean> binding) {
		CoreBooleanLabelColumn<T, S> column = new CoreBooleanLabelColumn<T, S>(headerModel, binding);
		columns.put(column, null);
		return new AddedBooleanLabelColumnState(column);
	}
	
	@Override
	public CustomizableToolbarBuilder<T, S> addTopToolbar() {
		CustomizableToolbarBuilder<T, S> builder = new CustomizableToolbarBuilder<T, S>(this);
		topToolbarBuilders.add(builder);
		return builder;
	}
	
	@Override
	public CustomizableToolbarBuilder<T, S> addBottomToolbar() {
		CustomizableToolbarBuilder<T, S> builder = new CustomizableToolbarBuilder<T, S>(this);
		bottomToolbarBuilders.add(builder);
		return builder;
	}
	
	@Override
	public ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn() {
		return addActionColumn(Model.of(""));
	}
	
	@Override
	public ActionColumnBuilder<T,  IAddedCoreColumnState<T, S>> addActionColumn(final IModel<String> headerLabelModel) {
		return new ActionColumnBuilder<T, IAddedCoreColumnState<T, S>>() {
			@Override
			protected IAddedCoreColumnState<T, S> onEnd(List<AbstractActionColumnElementBuilder<T, ?, ?>> builders) {
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
	@Deprecated
	public DataTableBuilder<T, S> hideTopToolbar() {
		return hideHeadersToolbar();
	}
	
	@Override
	public DataTableBuilder<T, S> hideNoRecordsToolbar() {
		showBottomToolbar = false;
		return this;
	}
	
	@Override
	@Deprecated
	public DataTableBuilder<T, S> hideBottomToolbar() {
		return hideNoRecordsToolbar();
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
		CoreDataTable<T, S> dataTable = factory.create(id, columns, sequenceProvider, rowsPerPage);
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
			dataTable.addBodyBottomToolbar(new CoreNoRecordsToolbar(dataTable, new ResourceModel(noRecordsResourceKey != null ? noRecordsResourceKey : "common.emptyList")));
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
	public IDecoratedBuildState<T, S> bootstrapPanel() {
		return new BootstrapPanelBuildState();
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
		public IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Renderer<? super T> renderer) {
			return DataTableBuilder.this.addLabelColumn(headerModel, renderer);
		}

		@Override
		public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, AbstractCoreBinding<? super T, C> binding) {
			return DataTableBuilder.this.addLabelColumn(headerModel, binding);
		}

		@Override
		public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, AbstractCoreBinding<? super T, C> binding,
				Renderer<? super C> renderer) {
			return DataTableBuilder.this.addLabelColumn(headerModel, binding, renderer);
		}

		@Override
		public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Function<? super T, C> function) {
			return DataTableBuilder.this.addLabelColumn(headerModel, function);
		}

		@Override
		public <C> IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, Function<? super T, C> function,
				Renderer<? super C> renderer) {
			return DataTableBuilder.this.addLabelColumn(headerModel, function, renderer);
		}

		@Override
		public IAddedLabelColumnState<T, S> addLabelColumn(IModel<String> headerModel, AbstractCoreBinding<? super T, ? extends Date> binding,
				IDatePattern datePattern) {
			return DataTableBuilder.this.addLabelColumn(headerModel, binding, datePattern);
		}

		@Override
		public <C> IAddedCoreColumnState<T, S> addBootstrapLabelColumn(IModel<String> headerModel, AbstractCoreBinding<? super T, C> binding,
				BootstrapRenderer<? super C> renderer) {
			return DataTableBuilder.this.addBootstrapLabelColumn(headerModel, binding, renderer);
		}
		
		@Override
		public <C> IAddedCoreColumnState<T, S> addBootstrapLabelColumn(IModel<String> headerModel,
				Function<? super T, C> function, BootstrapRenderer<? super C> renderer) {
			return DataTableBuilder.this.addBootstrapLabelColumn(headerModel, function, renderer);
		}

		@Override
		public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel, AbstractCoreBinding<? super T, C> binding,
				BootstrapRenderer<? super C> renderer) {
			return DataTableBuilder.this.addBootstrapBadgeColumn(headerModel, binding, renderer);
		}
		
		@Override
		public <C> IAddedBootstrapBadgeColumnState<T, S, C> addBootstrapBadgeColumn(IModel<String> headerModel,
				Function<? super T, C> function, BootstrapRenderer<? super C> renderer) {
			return DataTableBuilder.this.addBootstrapBadgeColumn(headerModel, function, renderer);
		}
		
		@Override
		public <C> IAddedBooleanLabelColumnState<T, S> addBooleanLabelColumn(IModel<String> headerModel,
				final AbstractCoreBinding<? super T, Boolean> binding) {
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
		public ActionColumnBuilder<T, IAddedCoreColumnState<T, S>> addActionColumn(IModel<String> headerLabelModel) {
			return DataTableBuilder.this.addActionColumn(headerLabelModel);
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
		@Deprecated
		public IBuildState<T, S> hideTopToolbar() {
			return DataTableBuilder.this.hideTopToolbar();
		}
		
		@Override
		public IBuildState<T, S> hideNoRecordsToolbar() {
			return DataTableBuilder.this.hideNoRecordsToolbar();
		}
		
		@Override
		@Deprecated
		public IBuildState<T, S> hideBottomToolbar() {
			return DataTableBuilder.this.hideBottomToolbar();
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
		public IDecoratedBuildState<T, S> bootstrapPanel() {
			return DataTableBuilder.this.bootstrapPanel();
		}
	}

	private abstract class AddedColumnState<NextState extends IAddedColumnState<T, S>> extends DataTableBuilderWrapper implements IAddedColumnState<T, S> {
		
		protected abstract IColumn<T, S> getColumn();
		
		protected abstract NextState getNextState();

		@Override
		public NextState when(Condition condition) {
			columns.put(getColumn(), condition);
			return getNextState();
		}
		
	}
	
	private abstract class AddedCoreColumnState<NextState extends IAddedCoreColumnState<T, S>> extends AddedColumnState<NextState>
			implements IAddedCoreColumnState<T, S> {
		
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

	private class AddedLabelColumnState extends AddedCoreColumnState<IAddedLabelColumnState<T, S>> implements IAddedLabelColumnState<T, S> {
		
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
		public <C> IAddedLabelColumnState<T, S> withTooltip(Function<? super T, C> function, Renderer<? super C> tooltipRenderer) {
			return withTooltip(tooltipRenderer.onResultOf(function));
		}
		
		@Deprecated
		@Override
		public IAddedLabelColumnState<T, S> withLink(LinkGeneratorFactory<T> linkGeneratorFactory) {
			return withLink(new LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter<>(linkGeneratorFactory));
		}

		@Deprecated
		@Override
		public <C> IAddedLabelColumnState<T, S> withLink(AbstractCoreBinding<? super T, C> binding, LinkGeneratorFactory<C> linkGeneratorFactory) {
			return withLink(binding, new LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter<>(linkGeneratorFactory));
		}

		@Deprecated
		@Override
		public IAddedLabelColumnState<T, S> withSideLink(LinkGeneratorFactory<T> sideLinkGeneratorFactory) {
			return withSideLink(new LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter<>(sideLinkGeneratorFactory));
		}

		@Deprecated
		@Override
		public <C> IAddedLabelColumnState<T, S> withSideLink(AbstractCoreBinding<? super T, C> binding, LinkGeneratorFactory<C> linkGeneratorFactory) {
			return withSideLink(binding, new LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter<>(linkGeneratorFactory));
		}
		
		@Override
		public IAddedLabelColumnState<T, S> withLink(
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper) {
			getColumn().setLinkGeneratorMapper(linkGeneratorMapper);
			return this;
		}
		
		@Override
		public <C> IAddedLabelColumnState<T, S> withLink(Function<? super T, C> function,
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
			return withLink(new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
		}

		@Override
		public <C> IAddedLabelColumnState<T, S> withLink(AbstractCoreBinding<? super T, C> binding,
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
		public <C> IAddedLabelColumnState<T, S> withSideLink(Function<? super T, C> function,
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
			return withSideLink(new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
		}

		@Override
		public <C> IAddedLabelColumnState<T, S> withSideLink(AbstractCoreBinding<? super T, C> binding,
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> linkGeneratorMapper) {
			return withSideLink(new BindingOneParameterLinkDescriptorMapper<>(binding, linkGeneratorMapper));
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

	private class AddedBootstrapBadgeColumnState<C> extends AddedCoreColumnState<IAddedBootstrapBadgeColumnState<T, S, C>> implements IAddedBootstrapBadgeColumnState<T, S, C> {
		
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
		public IAddedBootstrapBadgeColumnState<T, S, C> withLink(
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper) {
			getColumn().setLinkGeneratorMapper(linkGeneratorMapper);
			return this;
		}
		
		@Override
		public <E> IAddedBootstrapBadgeColumnState<T, S, C> withLink(Function<? super T, E> function,
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
			return withLink(new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
		}

		@Override
		public <E> IAddedBootstrapBadgeColumnState<T, S, C> withLink(AbstractCoreBinding<? super T, E> binding,
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
		public <E> IAddedBootstrapBadgeColumnState<T, S, C> withSideLink(Function<? super T, E> function,
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
			return withSideLink(new FunctionOneParameterLinkDescriptorMapper<>(function, linkGeneratorMapper));
		}

		@Override
		public <E> IAddedBootstrapBadgeColumnState<T, S, C> withSideLink(AbstractCoreBinding<? super T, E> binding,
				ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<E>> linkGeneratorMapper) {
			return withSideLink(new BindingOneParameterLinkDescriptorMapper<>(binding, linkGeneratorMapper));
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

	private class AddedBooleanLabelColumnState extends AddedCoreColumnState<IAddedBooleanLabelColumnState<T, S>> implements IAddedBooleanLabelColumnState<T, S> {
		
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
		private static final int DEFAULT_PAGER_VIEW_SIZE = 11;
		
		protected String countResourceKey = null;
		
		protected Condition responsiveCondition = Condition.alwaysTrue();
		
		protected final Multimap<AddInPlacement, IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>>
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
		public IActionColumnNoParameterBuildState<Void, IDecoratedBuildState<T, S>> actions(final AddInPlacement placement) {
			return new ActionColumnBuilder<Void, IDecoratedBuildState<T, S>>() {
				@Override
				protected IDecoratedBuildState<T, S> onEnd(List<AbstractActionColumnElementBuilder<Void, ?, ?>> builders) {
					for (final IOneParameterComponentFactory<?, IModel<Void>> builder : builders) {
						addIn(placement, ComponentFactories.ignoreParameter(
								new AbstractComponentFactory<Component>() {
									private static final long serialVersionUID = 1L;
									@Override
									public Component create(String wicketId) {
										return builder.create(wicketId, null);
									}
								}
						));
					}
					return DecoratedBuildState.this;
				}
			};
		}
		
		@Override
		public <Z> IActionColumnBuildState<Z, IDecoratedBuildState<T, S>> actions(final AddInPlacement placement, final IModel<Z> model) {
			return new ActionColumnBuilder<Z, IDecoratedBuildState<T, S>>() {
				@Override
				protected IDecoratedBuildState<T, S> onEnd(List<AbstractActionColumnElementBuilder<Z, ?, ?>> builders) {
					for (final IOneParameterComponentFactory<?, IModel<Z>> builder : builders) {
						addIn(placement, ComponentFactories.ignoreParameter(
								new AbstractComponentFactory<Component>() {
									private static final long serialVersionUID = 1L;
									@Override
									public Component create(String wicketId) {
										return builder.create(wicketId, model);
									}
								}
						));
					}
					return DecoratedBuildState.this;
				}
			};
		}
		
		@Override
		public IDecoratedBuildState<T, S> title(String resourceKey) {
			return addIn(AddInPlacement.HEADING_LEFT, new LabelAddInComponentFactory(new ResourceModel(resourceKey)), getTitleCssClass());
		}
		
		@Override
		public IDecoratedBuildState<T, S> title(IModel<?> model) {
			return addIn(AddInPlacement.HEADING_LEFT, new LabelAddInComponentFactory(model), getTitleCssClass());
		}
		
		@Override
		public IDecoratedBuildState<T, S> title(IComponentFactory<?> addInComponentFactory) {
			return addIn(AddInPlacement.HEADING_LEFT, ComponentFactories.ignoreParameter(addInComponentFactory), getTitleCssClass());
		}
		
		@Override
		public IDecoratedBuildState<T, S> count(String countResourceKey) {
			return count(AddInPlacement.HEADING_LEFT, countResourceKey);
		}
		
		@Override
		public IDecoratedBuildState<T, S> count(AddInPlacement placement, String countResourceKey) {
			this.countResourceKey = countResourceKey;
			return addIn(placement, new CountAddInComponentFactory(sequenceProvider, countResourceKey), getTitleCssClass());
		}
		
		@Override
		public IDecoratedBuildState<T, S> pagers() {
			return pagers(DEFAULT_PAGER_VIEW_SIZE);
		}
		
		@Override
		public IDecoratedBuildState<T, S> pagers(int viewSize) {
			return pager(AddInPlacement.HEADING_RIGHT, viewSize).pager(AddInPlacement.FOOTER_RIGHT, viewSize);
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
			return ajaxPager(AddInPlacement.HEADING_RIGHT, viewSize).ajaxPager(AddInPlacement.FOOTER_RIGHT, viewSize);
		}
		
		@Override
		public IDecoratedBuildState<T, S> ajaxPager(AddInPlacement placement) {
			return ajaxPager(placement, DEFAULT_PAGER_VIEW_SIZE);
		}
		
		@Override
		public IDecoratedBuildState<T, S> ajaxPager(AddInPlacement placement, int viewSize) {
			return addIn(placement, new AjaxPagerAddInComponentFactory(viewSize), getPaginationCssClass());
		}
		
		@Override
		public IDecoratedBuildState<T, S> addIn(AddInPlacement placement, IComponentFactory<?> addInComponentFactory) {
			return addIn(placement, ComponentFactories.ignoreParameter(addInComponentFactory));
		}
		
		@Override
		public IDecoratedBuildState<T, S> addIn(AddInPlacement placement, IComponentFactory<?> addInComponentFactory,
				String cssClasses) {
			return addIn(placement, ComponentFactories.ignoreParameter(addInComponentFactory), cssClasses);
		}
		
		@Override
		public IDecoratedBuildState<T, S> addIn(AddInPlacement placement,
				IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>> addInComponentFactory) {
			addInComponentFactories.put(placement, addInComponentFactory);
			return this;
		}
		
		@Override
		public IDecoratedBuildState<T, S> addIn(AddInPlacement placement,
				IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>> addInComponentFactory, String cssClasses) {
			return addIn(placement, new ClassAttributeAppenderDecoratingParameterizedComponentFactory<>(addInComponentFactory, cssClasses));
		}

		@Override
		public DecoratedCoreDataTablePanel<T, S> build(String id) {
			return build(id, Long.MAX_VALUE);
		}
		
		@Override
		public DecoratedCoreDataTablePanel<T, S> build(String id, long rowsPerPage) {
			DecoratedCoreDataTablePanel<T, S> panel = new DecoratedCoreDataTablePanel<T, S>(
					id,
					factory,
					columns,
					sequenceProvider,
					rowsPerPage,
					addInComponentFactories,
					responsiveCondition
			);
			
			if (noRecordsResourceKey == null && countResourceKey != null) {
				withNoRecordsResourceKey(countResourceKey + ".zero");
			}
			
			DataTableBuilder.this.finalizeBuild(panel.getDataTable());
			
			return panel;
		}
	}

	private class BootstrapPanelBuildState extends DecoratedBuildState {
		
		@Override
		protected String getTitleCssClass() {
			return "panel-title";
		}
		
		@Override
		protected String getPaginationCssClass() {
			return "add-in-pagination add-in-pagination-panel";
		}
		
		@Override
		public DecoratedCoreDataTablePanel<T, S> build(String id, long rowsPerPage) {
			BootstrapPanelCoreDataTablePanel<T, S> panel = new BootstrapPanelCoreDataTablePanel<T, S>(id, factory,
					columns, sequenceProvider, rowsPerPage, addInComponentFactories, responsiveCondition);
			if (noRecordsResourceKey == null && countResourceKey != null) {
				withNoRecordsResourceKey(countResourceKey + ".zero");
			}
			DataTableBuilder.this.finalizeBuild(panel.getDataTable());
			return panel;
		}
	}
	
	private static class ClassAttributeAppenderDecoratingParameterizedComponentFactory<C extends Component, P>
			extends AbstractDecoratingParameterizedComponentFactory<C, P> {
		
		private static final long serialVersionUID = 4455974327179014829L;
		
		private final String cssClass;

		public ClassAttributeAppenderDecoratingParameterizedComponentFactory(IOneParameterComponentFactory<? extends C, ? super P> delegate, String cssClass) {
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
