package org.iglooproject.wicket.more.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;

/**
 * A wrapper on Collection models to display them in Wicket components, such as
 * ListView.
 * <p>The returned list is {@link Collections#unmodifiableList(List) not modifiable}. 
 * <p>WARNING : depending on the implementation of the underlying collection, the index of
 * items in the returned list may not be the same on each call to getObject() if
 * the collection has been reloaded, even with the same content. To avoid any problem,
 * either use {@link SortedSet sorted collections} or, if using this model in a
 * {@link ListView}, override {@link ListView#getListItemModel()}.
 * <p>As this model brutally copies the elements to a new list from the underlying
 * collection, it may expose performance issues if used extensively or on very large
 * collections.
 */
public class CollectionToListWrapperModel<T> implements IModel<List<T>> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends Collection<? extends T>> wrappedModel;
	
	public static <T> CollectionToListWrapperModel<T> of(IModel<? extends Collection<? extends T>> model) {
		return new CollectionToListWrapperModel<>(model);
	}

	public static <T> CollectionToListWrapperModel<T> of(Collection<T> object) {
		return new CollectionToListWrapperModel<>(Model.of(object));
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
		} else if (collection instanceof List) {
			return Collections.unmodifiableList((List<? extends T>)collection);
		} else {
			return Collections.unmodifiableList(Lists.newArrayList(collection));
		}
	}
	
	@Override
	public void detach() {
		IModel.super.detach();
		wrappedModel.detach();
	}

}
