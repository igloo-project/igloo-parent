package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.IDataTableFactory;

public interface IBuildState<T, S extends ISort<?>> {
	
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
