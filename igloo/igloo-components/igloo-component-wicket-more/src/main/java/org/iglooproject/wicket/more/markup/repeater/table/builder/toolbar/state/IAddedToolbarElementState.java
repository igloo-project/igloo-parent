package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state;

import igloo.wicket.condition.Condition;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IAddedToolbarElementState<T, S extends ISort<?>>
    extends IToolbarElementState<T, S> {

  IAddedToolbarElementState<T, S> when(Condition condition);

  IAddedToolbarElementState<T, S> withClass(String cssClass);

  IAddedToolbarElementState<T, S> colspan(Integer colspan);

  IAddedToolbarElementState<T, S> full();
}
