package org.iglooproject.wicket.more.markup.repeater.table.builder.table.state;

import java.util.Collection;
import org.apache.wicket.behavior.Behavior;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IBuildState;

public interface IDataTableTableState<T, S extends ISort<?>> {

  IDataTableTableState<T, S> add(Collection<Behavior> tableBehaviors);

  IDataTableTableState<T, S> add(Behavior firstRowsBehavior, Behavior... otherRowsBehaviors);

  IDataTableTableState<T, S> fixed();

  IBuildState<T, S> end();
}
