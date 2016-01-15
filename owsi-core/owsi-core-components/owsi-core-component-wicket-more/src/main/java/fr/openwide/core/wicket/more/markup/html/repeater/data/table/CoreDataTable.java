package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import java.util.List;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public class CoreDataTable<T, S extends ISort<?>> extends DataTable<T, S> {

	private static final long serialVersionUID = 1L;
	
	private final Map<IColumn<T, S>, Condition> columns;
	
	private final List<IColumn<T, S>> columnList;
	
	private final CoreToolbarsContainer bodyBottomToolbars;

	private MarkupContainer componentToRefresh;
	
	public CoreDataTable(String id, Map<IColumn<T, S>, Condition> columns, IDataProvider<T> dataProvider, long rowsPerPage) {
		this(id, columns, Lists.newArrayList(columns.keySet()), dataProvider, rowsPerPage);
	}
	
	private CoreDataTable(String id, Map<IColumn<T, S>, Condition> columns,
			List<IColumn<T, S>> columnsList, IDataProvider<T> dataProvider, long rowsPerPage) {
		super(id, columnsList, dataProvider, rowsPerPage);
		this.columns = columns;
		this.columnList = columnsList;
		this.bodyBottomToolbars = new CoreToolbarsContainer("bodyBottomToolbars");
		getBody().add(bodyBottomToolbars);
		setComponentToRefresh(this);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		columnList.clear();
		for (Map.Entry<IColumn<T, S>, Condition> entry : columns.entrySet()) {
			Condition condition = entry.getValue();
			if (condition == null || condition.applies()) {
				IColumn<T, S> column = entry.getKey();
				columnList.add(column);
			}
		}
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		for (Map.Entry<? extends IDetachable, ? extends IDetachable> entry : columns.entrySet()) {
			entry.getKey().detach();
			IDetachable value = entry.getValue();
			if (value != null) {
				value.detach();
			}
		}
	}

	public void addBodyBottomToolbar(final AbstractToolbar toolbar) {
		Args.notNull(toolbar, "toolbar");
		bodyBottomToolbars.getRepeatingView().add(toolbar);
	}
	
	@Override
	protected Item<IColumn<T, S>> newCellItem(String id, int index, IModel<IColumn<T, S>> model) {
		Item<IColumn<T, S>> cellItem = super.newCellItem(id, index, model);
		cellItem.setOutputMarkupId(true);
		return cellItem;
	}
	
	@Override
	protected Item<T> newRowItem(String id, int index, IModel<T> model) {
		Item<T> rowItem = super.newRowItem(id, index, model);
		rowItem.setOutputMarkupId(true);
		return rowItem;
	}

	public Map<IColumn<T, S>, Condition> getColumnsConditions() {
		return columns;
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

}
