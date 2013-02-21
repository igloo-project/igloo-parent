package fr.openwide.core.wicket.more.model;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableList;

/**
 * A wrapper on Collection models to display them in Wicket components, such as
 * ListView. <br>
 * WARNING : depending on the implementation of the underlying collection, the index of
 * items in the returned list may not be the same on each call to getObject() if
 * the collection has been reloaded, even with the same content. To avoid any problem,
 * either use {@link SortedSet sorted collections} or, if using this model in a
 * {@link ListView}, override {@link ListView#getListItemModel()}. <br>
 * As this model brutally copies the elements to a new list from the underlying
 * collection, it may expose performance issues if used extensively or on very large
 * collections.
 */
public class CollectionToListWrapperModel<T> extends AbstractReadOnlyModel<List<T>> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends Collection<? extends T>> wrappedModel;
	
	public static <T> CollectionToListWrapperModel<T> of(IModel<? extends Collection<? extends T>> model) {
		return new CollectionToListWrapperModel<T>(model);
	}

	public static <T> CollectionToListWrapperModel<T> of(Collection<T> object) {
		return new CollectionToListWrapperModel<T>(Model.of(object));
	}

	public CollectionToListWrapperModel(IModel<? extends Collection<? extends T>> wrappedModel) {
		super();
		this.wrappedModel = wrappedModel;
	}

	@Override
	public List<T> getObject() {
		Collection<? extends T> collection = wrappedModel.getObject();
		if (collection == null) {
			return null;
		}
		return ImmutableList.copyOf(collection);
	}
	
	@Override
	public void detach() {
		super.detach();
		wrappedModel.detach();
	}

}
