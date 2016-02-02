package fr.openwide.core.wicket.more.markup.repeater.table.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.markup.repeater.table.AbstractCoreToolbar;
import fr.openwide.core.wicket.more.markup.repeater.table.CoreDataTable;

public class CoreNoRecordsToolbar extends AbstractCoreToolbar {

	private static final long serialVersionUID = -6103179706160240604L;

	public CoreNoRecordsToolbar(final CoreDataTable<?, ?> table, final IModel<String> messageModel) {
		super(table);

		WebMarkupContainer td = new WebMarkupContainer("td");
		add(td);

		td.add(AttributeModifier.replace("colspan", new AbstractReadOnlyModel<String>() {
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
