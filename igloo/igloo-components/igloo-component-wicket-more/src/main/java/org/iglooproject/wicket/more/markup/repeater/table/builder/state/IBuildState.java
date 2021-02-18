package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;
import org.iglooproject.wicket.more.markup.repeater.table.builder.IDataTableFactory;
import org.iglooproject.wicket.more.markup.repeater.table.builder.rows.state.IDataTableRowsState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.table.state.IDataTableTableState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.CustomizableToolbarBuilder;

public interface IBuildState<T, S extends ISort<?>> {

	CustomizableToolbarBuilder<T, S> addTopToolbar();

	CustomizableToolbarBuilder<T, S> addBottomToolbar();

	IDataTableRowsState<T, S> rows();

	IDataTableTableState<T, S> table();

	IBuildState<T, S> withNoRecordsResourceKey(String noRecordsResourceKey);

	IBuildState<T, S> hideHeadersToolbar();

	IBuildState<T, S> hideNoRecordsToolbar();

	IBuildState<T, S> withFactory(IDataTableFactory<T, S> factory);

	CoreDataTable<T, S> build(String id);

	CoreDataTable<T, S> build(String id, long rowsPerPage);

	IDecoratedBuildState<T, S> decorate();

	IDecoratedBuildState<T, S> bootstrapCard();

}
