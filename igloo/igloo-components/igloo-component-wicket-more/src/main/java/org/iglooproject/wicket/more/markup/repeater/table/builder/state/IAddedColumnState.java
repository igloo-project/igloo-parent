package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import igloo.wicket.condition.Condition;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IAddedColumnState<T, S extends ISort<?>> extends IColumnState<T, S> {

  IAddedColumnState<T, S> when(Condition condition);
}
