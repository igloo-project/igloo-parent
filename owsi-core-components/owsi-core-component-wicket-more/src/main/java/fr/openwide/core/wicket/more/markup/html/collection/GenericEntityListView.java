package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

/**
 * A {@link ListView} akin to {@link AbstractGenericEntityCollectionView}, which is itself a {@link RefreshingView}.
 */
public abstract class GenericEntityListView<T extends GenericEntity<?, ?>> extends ListView<T> {

	private static final long serialVersionUID = 1L;
	
	public GenericEntityListView(String id, IModel<? extends List<? extends T>> model) {
		super(id, model);
	}

	public GenericEntityListView(String id, List<? extends T> list) {
		super(id, list);
	}

	@Override
	protected IModel<T> getListItemModel(IModel<? extends List<T>> listViewModel, int index) {
		List<T> list = listViewModel.getObject();
		return getItemModel(list == null ? null : list.get(index));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) // Works around restrictions on GenericEntityModel that seem too strong.
	protected IModel<T> getItemModel(T object) {
		return GenericEntityModel.of((GenericEntity)object);
	}

}
