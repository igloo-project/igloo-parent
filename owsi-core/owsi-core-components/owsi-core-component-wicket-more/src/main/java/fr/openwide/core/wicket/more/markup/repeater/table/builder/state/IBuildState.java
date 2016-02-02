package fr.openwide.core.wicket.more.markup.repeater.table.builder.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.repeater.table.CoreDataTable;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.IDataTableFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.ActionColumnBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.toolbar.CustomizableToolbarBuilder;

public interface IBuildState<T, S extends ISort<?>> {
	
	CustomizableToolbarBuilder<T, S> addTopToolbar();
	
	CustomizableToolbarBuilder<T, S> addBottomToolbar();
	
	ActionColumnBuilder<T, S> addActionColumn();
	
	ActionColumnBuilder<T, S> addActionColumn(IModel<String> headerLabelModel);
	
	IBuildState<T, S> withNoRecordsResourceKey(String noRecordsResourceKey);

	IBuildState<T, S> hideHeadersToolbar();
	
	/**
	 * @deprecated Use {@link #hideHeadersToolbar()} instead.
	 */
	@Deprecated
	IBuildState<T, S> hideTopToolbar();
	
	IBuildState<T, S> hideNoRecordsToolbar();

	/**
	 * @deprecated Use {@link #hideNoRecordsToolbar()} instead.
	 */
	@Deprecated
	IBuildState<T, S> hideBottomToolbar();
	
	IBuildState<T, S> withFactory(IDataTableFactory<T, S> factory);
	
	CoreDataTable<T, S> build(String id);
	
	CoreDataTable<T, S> build(String id, long rowsPerPage);
	
	IDecoratedBuildState<T, S> decorate();
	
	IDecoratedBuildState<T, S> bootstrapPanel();

}
