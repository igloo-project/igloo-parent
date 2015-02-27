package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;

public interface IBuildState<T, S extends ISort<?>> {
	
	IBuildState<T, S> withNoRecordsResourceKey(String noRecordsResourceKey);
	
	CoreDataTable<T, S> build(String id);
	
	CoreDataTable<T, S> build(String id, long rowsPerPage);
	
	IDecoratedBuildState<T, S> decorate();
	
	IDecoratedBuildState<T, S> bootstrapPanel();
	
}
