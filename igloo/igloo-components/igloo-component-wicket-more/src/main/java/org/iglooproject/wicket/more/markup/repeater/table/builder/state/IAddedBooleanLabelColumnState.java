package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import org.iglooproject.jpa.more.business.sort.ISort;

public interface IAddedBooleanLabelColumnState<T, S extends ISort<?>>
    extends IAddedCoreColumnState<T, S> {

  IAddedBooleanLabelColumnState<T, S> hideIfNullOrFalse();
}
