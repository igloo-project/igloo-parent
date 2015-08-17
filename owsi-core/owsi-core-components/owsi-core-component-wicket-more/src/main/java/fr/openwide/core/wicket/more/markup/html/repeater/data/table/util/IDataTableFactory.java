package fr.openwide.core.wicket.more.markup.html.repeater.data.table.util;

import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.util.io.IClusterable;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;

public interface IDataTableFactory<T, S extends ISort<?>> extends IClusterable {

	CoreDataTable<T, S> create(String id, Map<IColumn<T, S>, Condition> columns, IDataProvider<T> dataProvider, long rowsPerPage);

}
