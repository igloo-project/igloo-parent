package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar.state;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;

public interface IToolbarElementState<T, S extends ISort<?>> extends IToolbarBuildState<T, S> {

	IAddedToolbarLabelElementState<T, S> addLabel(IModel<String> model);

	IAddedToolbarCoreElementState<T, S> addComponent(IOneParameterComponentFactory<Component, CoreDataTable<T, S>> delegateFactory);

}
