package fr.openwide.core.wicket.more.markup.repeater.table.builder;

import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.util.io.IClusterable;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.repeater.sequence.ISequenceProvider;
import fr.openwide.core.wicket.more.markup.repeater.table.CoreDataTable;

public interface IDataTableFactory<T, S extends ISort<?>> extends IClusterable {

	CoreDataTable<T, S> create(String id, Map<IColumn<T, S>, Condition> columns, ISequenceProvider<T> sequenceProvider, long rowsPerPage);

}
