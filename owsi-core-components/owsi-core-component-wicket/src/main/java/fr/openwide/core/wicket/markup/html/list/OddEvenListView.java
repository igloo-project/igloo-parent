package fr.openwide.core.wicket.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

public abstract class OddEvenListView<T> extends ListView<T> {

	private static final long serialVersionUID = -7408299272975588957L;
	
	public OddEvenListView(final String id, final IModel<List<T>> model) {
		super(id, model);
	}

	protected ListItem<T> newItem(final int index) {
		return new OddEvenListItem<T>(index, getListItemModel(getModel(), index));
	}

}
