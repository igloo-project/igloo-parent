package org.iglooproject.wicket.more.markup.repeater.table;

import java.util.List;
import java.util.Map;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;
import org.iglooproject.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import org.iglooproject.wicket.more.markup.repeater.sequence.ISequenceProvider;
import org.iglooproject.wicket.more.markup.repeater.table.builder.IDataTableFactory;

import com.google.common.collect.Multimap;

public class BootstrapCardCoreDataTablePanel<T, S extends ISort<?>> extends DecoratedCoreDataTablePanel<T, S> {

	private static final long serialVersionUID = -5953180156268489658L;

	public BootstrapCardCoreDataTablePanel(
		String id,
		IDataTableFactory<T, S> factory,
		Map<IColumn<T, S>, Condition> columns,
		ISequenceProvider<T> sequenceProvider,
		List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> rowsBehaviorFactories,
		List<Behavior> tableBehaviors,
		long rowsPerPage,
		Multimap<AddInPlacement, ? extends IOneParameterComponentFactory<?, ? super DecoratedCoreDataTablePanel<T, S>>> addInComponentFactories,
		Condition responsiveCondition
	) {
		super(id, factory, columns, sequenceProvider, rowsBehaviorFactories, tableBehaviors, rowsPerPage,
				addInComponentFactories, responsiveCondition);
	}

}
