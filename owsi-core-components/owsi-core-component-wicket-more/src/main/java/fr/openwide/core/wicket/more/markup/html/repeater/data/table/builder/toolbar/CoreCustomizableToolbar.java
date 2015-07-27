package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;

public class CoreCustomizableToolbar<T, S extends ISort<?>> extends AbstractToolbar {

	private static final long serialVersionUID = 5382092664865344556L;

	public CoreCustomizableToolbar(final CoreDataTable<T, S> dataTable, final List<CustomizableToolbarElementFactory<T, S>> factories) {
		super(dataTable);

		RefreshingView<CustomizableToolbarElementFactory<T, S>> headers = new RefreshingView<CustomizableToolbarElementFactory<T, S>>("headers") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Iterator<IModel<CustomizableToolbarElementFactory<T, S>>> getItemModels() {
				List<IModel<CustomizableToolbarElementFactory<T, S>>> models = new LinkedList<IModel<CustomizableToolbarElementFactory<T, S>>>();
				
				for (CustomizableToolbarElementFactory<T, S> factory : factories) {
					models.add(Model.of(factory));
				}
				
				return models.iterator();
			}

			@Override
			protected void populateItem(Item<CustomizableToolbarElementFactory<T, S>> item) {
				final CustomizableToolbarElementFactory<T, S> factory = item.getModelObject();
				item.add(factory.create("header", dataTable));
			}
		};
		add(headers);
	}

}
