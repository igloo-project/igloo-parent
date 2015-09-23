package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

/**
 * A {@link ListView} akin to {@link AbstractGenericEntityCollectionView}, which is itself a {@link RefreshingView}.
 */
public abstract class GenericEntityListView<T extends GenericEntity<?, ?>> extends PageableListView<T> {

	private static final long serialVersionUID = 1L;

	public GenericEntityListView(String id, IModel<? extends List<T>> model) {
		this(id, model, Long.MAX_VALUE);
	}
	
	public GenericEntityListView(String id, IModel<? extends List<T>> model, long itemsPerPage) {
		super(id, model, Integer.MAX_VALUE /* The constructor asks for an int, so we use the setter instead */);
		setItemsPerPage(itemsPerPage);
	}

	public GenericEntityListView(String id, List<T> list) {
		this(id, list, Long.MAX_VALUE);
	}

	public GenericEntityListView(String id, List<T> list, long itemsPerPage) {
		super(id, list, Integer.MAX_VALUE /* The constructor asks for an int, so we use the setter instead */);
		setItemsPerPage(itemsPerPage);
	}

	@Override
	protected IModel<T> getListItemModel(IModel<? extends List<T>> listViewModel, int index) {
		List<T> list = listViewModel.getObject();
		return getItemModel(list == null ? null : list.get(index));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) // Works around restrictions on GenericEntityModel that seem too strong.
	protected IModel<T> getItemModel(T object) {
		return (IModel<T>) GenericEntityModel.of((GenericEntity) object);
	}

}
