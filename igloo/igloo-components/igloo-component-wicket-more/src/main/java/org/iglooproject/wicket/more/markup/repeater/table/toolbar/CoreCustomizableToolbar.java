package org.iglooproject.wicket.more.markup.repeater.table.toolbar;

import java.util.List;

import org.apache.wicket.Component;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import org.iglooproject.wicket.more.markup.repeater.FactoryRepeatingView;
import org.iglooproject.wicket.more.markup.repeater.table.AbstractCoreToolbar;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;

public class CoreCustomizableToolbar<T, S extends ISort<?>> extends AbstractCoreToolbar {

	private static final long serialVersionUID = 5382092664865344556L;

	public CoreCustomizableToolbar(final CoreDataTable<T, S> dataTable,
			final List<? extends IOneParameterComponentFactory<Component, CoreDataTable<T, S>>> factories) {
		super(dataTable);

		FactoryRepeatingView headers = new FactoryRepeatingView("header");
		headers.addAll(factories, dataTable);
		headers.setOutputMarkupId(true);
		add(headers);
	}

}
