package org.iglooproject.wicket.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;

public abstract class OddEvenPageableListView<T> extends PageableListView<T> {

	private static final long serialVersionUID = 1195268737114390619L;

	public OddEvenPageableListView(final String id, final IModel<? extends List<T>> model, int rowsPerPage) {
		super(id, model, rowsPerPage);
	}

	@Override
	protected ListItem<T> newItem(final int index, IModel<T> itemModel) {
		return new OddEvenListItem<>(index, itemModel);
	}

}
