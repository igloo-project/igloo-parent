package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A {@link ListView} akin to {@link AbstractSerializedItemCollectionView}, which is itself a {@link RefreshingView}.
 */
public abstract class SerializedItemListView<T extends Serializable> extends ListView<T> {

	private static final long serialVersionUID = -8621785529210100553L;

	public SerializedItemListView(String id, IModel<? extends List<? extends T>> model) {
		super(id, model);
	}

	public SerializedItemListView(String id, List<? extends T> list) {
		super(id, list);
	}

	@Override
	protected IModel<T> getListItemModel(IModel<? extends List<T>> listViewModel, int index) {
		List<T> list = listViewModel.getObject();
		return getItemModel(list == null ? null : list.get(index));
	}

	protected IModel<T> getItemModel(T object) {
		return Model.of(object);
	}
}
