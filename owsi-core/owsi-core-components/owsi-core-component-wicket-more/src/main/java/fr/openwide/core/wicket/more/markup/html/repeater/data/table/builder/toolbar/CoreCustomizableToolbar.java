package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.markup.repeater.RepeatingView;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.ComponentFactories;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;

public class CoreCustomizableToolbar<T, S extends ISort<?>> extends AbstractToolbar {

	private static final long serialVersionUID = 5382092664865344556L;

	public CoreCustomizableToolbar(final CoreDataTable<T, S> dataTable,
			final List<? extends IOneParameterComponentFactory<Component, CoreDataTable<T, S>>> factories) {
		super(dataTable);

		RepeatingView headers = new RepeatingView("headers");
		ComponentFactories.addAll(headers, factories, dataTable);
		add(headers);
	}

}
