package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.wicket.more.markup.repeater.collection.CollectionView;
import fr.openwide.core.wicket.more.util.model.Models;

/**
 * A {@link ListView} akin to {@link AbstractSerializedItemCollectionView}, which is itself a {@link RefreshingView}.
 * @deprecated use {@link CollectionView} with {@link Models} instead : 
 * <pre>
 * {@code 
 * new SerializedItem*View<T>("id", model);
 * ->
 * new CollectionView<T>("id", model, Models.<T>serializableModelFactory());
 * }
 * </pre>
 */
@Deprecated
public abstract class SerializedItemListView<T extends Serializable> extends PageableListView<T> {

	private static final long serialVersionUID = -8621785529210100553L;

	public SerializedItemListView(String id, IModel<? extends List<T>> model) {
		this(id, model, Long.MAX_VALUE);
	}

	public SerializedItemListView(String id, IModel<? extends List<T>> model, long itemsPerPage) {
		super(id, model, Integer.MAX_VALUE /* The constructor asks for an int, so we use the setter instead */);
		setItemsPerPage(itemsPerPage);
	}

	public SerializedItemListView(String id, List<T> list) {
		this(id, list, Long.MAX_VALUE);
	}

	public SerializedItemListView(String id, List<T> list, long itemsPerPage) {
		super(id, list, Integer.MAX_VALUE /* The constructor asks for an int, so we use the setter instead */);
		setItemsPerPage(itemsPerPage);
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
