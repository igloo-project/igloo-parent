package fr.openwide.core.wicket.more.markup.repeater.table;

import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

import com.google.common.collect.Multimap;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.sequence.ISequenceProvider;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.IDataTableFactory;

public class BootstrapPanelCoreDataTablePanel<T, S extends ISort<?>> extends DecoratedCoreDataTablePanel<T, S> {

	private static final long serialVersionUID = -5953180156268489658L;

	public BootstrapPanelCoreDataTablePanel(
			String id,
			IDataTableFactory<T, S> factory,
			Map<IColumn<T, S>, Condition> columns,
			ISequenceProvider<T> sequenceProvider,
			long rowsPerPage,
			Multimap<AddInPlacement, ? extends IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>> addInComponentFactories) {
		super(id, factory, columns, sequenceProvider, rowsPerPage, addInComponentFactories);
	}
	
}
