package fr.openwide.core.wicket.more.markup.repeater.table.builder.toolbar.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;

public interface IToolbarBuildState<T, S extends ISort<?>> {

	DataTableBuilder<T, S> end();

}
