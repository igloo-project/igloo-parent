package org.iglooproject.wicket.more.markup.repeater.table.builder;

import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.util.io.IClusterable;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.repeater.sequence.ISequenceProvider;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;

import com.google.common.base.Function;

public interface IDataTableFactory<T, S extends ISort<?>> extends IClusterable {

	CoreDataTable<T, S> create(String id, Map<IColumn<T, S>, Condition> columns, ISequenceProvider<T> sequenceProvider,
			List<Function<T, String>> rowCssClassProviders, long rowsPerPage);

}
