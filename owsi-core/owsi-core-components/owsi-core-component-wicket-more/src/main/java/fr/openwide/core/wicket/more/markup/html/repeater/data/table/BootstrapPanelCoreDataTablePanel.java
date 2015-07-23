package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import com.google.common.collect.Multimap;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.util.IDataTableFactory;

public class BootstrapPanelCoreDataTablePanel<T, S extends ISort<?>> extends DecoratedCoreDataTablePanel<T, S> {

	private static final long serialVersionUID = -5953180156268489658L;

	public BootstrapPanelCoreDataTablePanel(
			String id,
			IDataTableFactory<T, S> factory,
			Map<IColumn<T, S>, Condition> columns,
			IDataProvider<T> dataProvider,
			long rowsPerPage,
			Multimap<AddInPlacement, ? extends IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>> addInComponentFactories) {
		super(id, factory, columns, dataProvider, rowsPerPage, addInComponentFactories);
	}
	
}
