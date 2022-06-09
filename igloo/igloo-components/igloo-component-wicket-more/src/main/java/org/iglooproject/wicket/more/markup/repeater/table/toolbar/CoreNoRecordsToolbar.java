package org.iglooproject.wicket.more.markup.repeater.table.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.repeater.table.AbstractCoreToolbar;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;

import igloo.wicket.component.CoreLabel;

public class CoreNoRecordsToolbar extends AbstractCoreToolbar {

	private static final long serialVersionUID = -6103179706160240604L;

	public CoreNoRecordsToolbar(final CoreDataTable<?, ?> table, final IModel<String> messageModel) {
		super(table);

		WebMarkupContainer td = new WebMarkupContainer("td");
		add(td);

		td.add(AttributeModifier.replace("colspan", new IModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject() {
				return String.valueOf(table.getDisplayedColumns().size());
			}
		}));
		td.add(new CoreLabel("msg", messageModel));
	}

	@Override
	public boolean isVisible() {
		return getTable().getRowCount() == 0;
	}

}
