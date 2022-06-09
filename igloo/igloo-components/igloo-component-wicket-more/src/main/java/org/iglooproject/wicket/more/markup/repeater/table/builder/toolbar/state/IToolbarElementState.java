package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;

import igloo.wicket.factory.IOneParameterComponentFactory;


public interface IToolbarElementState<T, S extends ISort<?>> extends IToolbarBuildState<T, S> {

	IAddedToolbarLabelElementState<T, S> addLabel(IModel<String> model);

	IAddedToolbarCoreElementState<T, S> addComponent(IOneParameterComponentFactory<Component, CoreDataTable<T, S>> delegateFactory);

	IToolbarElementState<T, S> hideIfEmpty();

}
