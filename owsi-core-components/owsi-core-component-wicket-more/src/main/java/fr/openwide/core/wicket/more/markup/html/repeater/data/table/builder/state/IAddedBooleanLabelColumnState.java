package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state;

import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IAddedBooleanLabelColumnState<T, S extends ISort<?>> extends IAddedCoreColumnState<T, S> {

	IAddedBooleanLabelColumnState<T, S> hideIfNullOrFalse();

}
