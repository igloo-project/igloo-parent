package org.iglooproject.wicket.more.markup.repeater.table;

import java.util.List;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageableItems;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;
import org.iglooproject.wicket.more.markup.repeater.sequence.ISequenceProvider;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceGridView;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.SequenceProviders;

import com.google.common.collect.Lists;

/**
 * A re-implementation of DataTable that accepts ISequenceProvider instead of IDataProvider.
 * <p>Also, it allows dynamic hiding of columns based on {@link Condition}s.
 */
public class CoreDataTable<T, S extends ISort<?>> extends Panel implements IPageableItems {

	private static final long serialVersionUID = 1L;

	private final Map<IColumn<T, S>, Condition> columnToConditionMap;

	private final List<IColumn<T, S>> displayedColumns;

	private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> rowsBehaviorFactories;

	private final SequenceGridView<T> gridView;

	private final WebMarkupContainer body;

	private final CoreToolbarsContainer topToolbars;

	private final CoreToolbarsContainer bodyBottomToolbars;

	private final CoreToolbarsContainer bottomToolbars;
	
	private int toolbarIdCounter = 0;

	private MarkupContainer componentToRefresh;
	
	public CoreDataTable(
		String id,
		Map<IColumn<T, S>, Condition> columns,
		IDataProvider<T> dataProvider,
		List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> rowsBehaviorFactories,
		List<Behavior> tableBehaviors,
		long rowsPerPage
	) {
		this(id, columns, SequenceProviders.forDataProvider(dataProvider), rowsBehaviorFactories, tableBehaviors, rowsPerPage);
	}
	
	public CoreDataTable(
		String id,
		Map<IColumn<T, S>, Condition> columns,
		ISequenceProvider<T> sequenceProvider,
		List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> rowsBehaviorFactories,
		List<Behavior> tableBehaviors,
		long rowsPerPage
	) {
		super(id);
		this.columnToConditionMap = columns;
		this.displayedColumns = Lists.newArrayList();
		this.rowsBehaviorFactories = rowsBehaviorFactories;
		
		body = newBodyContainer("body");
		add(body);
		
		gridView = newGridView("rows", displayedColumns, sequenceProvider);
		gridView.setItemsPerPage(rowsPerPage);
		body.add(gridView);
		
		bodyBottomToolbars = new CoreToolbarsContainer("bodyBottomToolbars");
		body.add(bodyBottomToolbars);
		
		topToolbars = new CoreToolbarsContainer("topToolbars");
		bottomToolbars = new CoreToolbarsContainer("bottomToolbars");
		add(topToolbars, bottomToolbars);
		
		add(tableBehaviors.stream().toArray(Behavior[]::new));
		
		setComponentToRefresh(this);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(columnToConditionMap);
		Detachables.detach(rowsBehaviorFactories);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		// Update the displayed columns
		displayedColumns.clear();
		for (Map.Entry<IColumn<T, S>, Condition> entry : columnToConditionMap.entrySet()) {
			Condition condition = entry.getValue();
			if (condition == null || condition.applies()) {
				IColumn<T, S> column = entry.getKey();
				displayedColumns.add(column);
			}
		}
	}

	@Override
	public long getCurrentPage() {
		return gridView.getCurrentPage();
	}

	@Override
	public void setCurrentPage(long page) {
		gridView.setCurrentPage(page);
		onPageChanged();
	}

	protected void onPageChanged() {
		// No-op by default
	}

	@Override
	public long getPageCount() {
		return gridView.getPageCount();
	}
	
	@Override
	public long getItemCount() {
		return gridView.getItemCount();
	}

	@Override
	public long getItemsPerPage() {
		return gridView.getItemsPerPage();
	}
	
	public long getRowCount() {
		return gridView.getRowCount();
	}

	@Override
	public void setItemsPerPage(long itemsPerPage) {
		gridView.setItemsPerPage(itemsPerPage);
	}
	
	public final CoreDataTable<T, S> setItemReuseStrategy(final IItemReuseStrategy strategy) {
		gridView.setItemReuseStrategy(strategy);
		return this;
	}
	
	public ISequenceProvider<T> getSequenceProvider() {
		return gridView.getSequenceProvider();
	}
	
	/**
	 * @see AbstractCoreToolbar
	 */
	String newToolbarId()
	{
		toolbarIdCounter++;
		return String.valueOf(toolbarIdCounter).intern();
	}

	private void addToolbar(final AbstractCoreToolbar toolbar, final CoreToolbarsContainer container) {
		Args.notNull(toolbar, "toolbar");
		container.getRepeatingView().add(toolbar);
	}

	public void addTopToolbar(final AbstractCoreToolbar toolbar) {
		addToolbar(toolbar, topToolbars);
	}

	public void addBodyBottomToolbar(final AbstractCoreToolbar toolbar) {
		Args.notNull(toolbar, "toolbar");
		bodyBottomToolbars.getRepeatingView().add(toolbar);
	}

	public void addBottomToolbar(final AbstractCoreToolbar toolbar) {
		addToolbar(toolbar, bottomToolbars);
	}

	protected WebMarkupContainer newBodyContainer(final String id) {
		return new WebMarkupContainer(id);
	}
	
	protected SequenceGridView<T> newGridView(String id, List<? extends IColumn<T, S>> columns,
			ISequenceProvider<T> sequenceProvider) {
		return new DefaultGridView(id, columns, sequenceProvider);
	}
	
	protected Item<IColumn<T, S>> newCellItem(String id, int index, IModel<IColumn<T, S>> model) {
		Item<IColumn<T, S>> cellItem = new Item<>(id, index, model);
		cellItem.setOutputMarkupId(true);
		return cellItem;
	}
	
	protected Item<T> newRowItem(String id, int index, IModel<T> model) {
		Item<T> rowItem = new Item<>(id, index, model);
		rowItem.setOutputMarkupId(true);
		return rowItem;
	}

	public Map<IColumn<T, S>, Condition> getColumnToConditionMap() {
		return columnToConditionMap;
	}
	
	public List<IColumn<T, S>> getDisplayedColumns() {
		return displayedColumns;
	}

	public MarkupContainer getComponentToRefresh() {
		return componentToRefresh;
	}
	
	public void setComponentToRefresh(MarkupContainer component) {
		Args.isTrue(
				component == this || component.contains(this, true),
				"The component to refresh in stead of a DataTable must contain the DataTable. {} does not contain {}",
				component, this
		);
		this.componentToRefresh = component;
		this.componentToRefresh.setOutputMarkupId(true);
	}

	protected class DefaultGridView extends SequenceGridView<T> {
		private static final long serialVersionUID = 1L;

		public DefaultGridView(String id, List<? extends IColumn<T, S>> displayedColumns, ISequenceProvider<T> sequenceProvider) {
			super(id, displayedColumns, sequenceProvider);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected Item newCellItem(final String id, final int index, final IModel model) {
			Item item = CoreDataTable.this.newCellItem(id, index, model);
			final ICellPopulator<T> column = internalGetPopulators().get(index);
			if (column instanceof IStyledColumn) {
				item.add(new ClassAttributeAppender(new IModel<String>() {
					private static final long serialVersionUID = 1L;
					@Override
					public String getObject() {
						return ((IStyledColumn<T, S>) column).getCssClass();
					}
				}));
			}
			return item;
		}

		@Override
		protected Item<T> newRowItem(final String id, final int index, final IModel<T> model) {
			Item<T> item = CoreDataTable.this.newRowItem(id, index, model);
			item.add(rowsBehaviorFactories.stream().map(f -> f.create(model)).toArray(Behavior[]::new));
			return item;
		}
	}

}
