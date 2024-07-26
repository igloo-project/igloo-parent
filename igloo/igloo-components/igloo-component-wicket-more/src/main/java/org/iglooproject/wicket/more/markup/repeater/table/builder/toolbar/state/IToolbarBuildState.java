package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;

public interface IToolbarBuildState<T, S extends ISort<?>> {

  DataTableBuilder<T, S> end();
}
