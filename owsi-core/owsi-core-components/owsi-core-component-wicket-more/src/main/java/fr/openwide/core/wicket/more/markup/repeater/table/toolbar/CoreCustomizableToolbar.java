package fr.openwide.core.wicket.more.markup.repeater.table.toolbar;

import java.util.List;

import org.apache.wicket.Component;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.FactoryRepeatingView;
import fr.openwide.core.wicket.more.markup.repeater.table.AbstractCoreToolbar;
import fr.openwide.core.wicket.more.markup.repeater.table.CoreDataTable;

public class CoreCustomizableToolbar<T, S extends ISort<?>> extends AbstractCoreToolbar {

	private static final long serialVersionUID = 5382092664865344556L;

	public CoreCustomizableToolbar(final CoreDataTable<T, S> dataTable,
			final List<? extends IOneParameterComponentFactory<Component, CoreDataTable<T, S>>> factories) {
		super(dataTable);

		FactoryRepeatingView headers = new FactoryRepeatingView("header");
		headers.addAll(factories, dataTable);
		add(headers);
	}

}
