package fr.openwide.core.wicket.more.markup.repeater.table.builder.state;

import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IAddedBooleanLabelColumnState<T, S extends ISort<?>> extends IAddedCoreColumnState<T, S> {

	IAddedBooleanLabelColumnState<T, S> hideIfNullOrFalse();

}
