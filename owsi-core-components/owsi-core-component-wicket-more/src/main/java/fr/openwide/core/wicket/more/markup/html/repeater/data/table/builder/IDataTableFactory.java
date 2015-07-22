package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder;

import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;

public interface IDataTableFactory<T, S extends ISort<?>> {

	public CoreDataTable<T, S> build(String id, Map<IColumn<T, S>, Condition> columns, IDataProvider<T> dataProvider, long rowsPerPage);

}
