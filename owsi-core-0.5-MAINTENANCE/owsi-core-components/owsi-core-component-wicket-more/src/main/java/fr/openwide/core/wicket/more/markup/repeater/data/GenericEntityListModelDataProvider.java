package fr.openwide.core.wicket.more.markup.repeater.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class GenericEntityListModelDataProvider<K extends Serializable & Comparable<K>,
		E extends GenericEntity<K, ?>> implements IDataProvider<E> {

	private static final long serialVersionUID = -238640572566596234L;
	
	private IModel<? extends List<E>> listModel;

	public GenericEntityListModelDataProvider(IModel<? extends List<E>> listModel) {
		if (listModel == null) {
			throw new IllegalArgumentException("listModel cannot be null");
		}
		
		this.listModel = listModel;
	}
	
	@Override
	public void detach() {
		listModel.detach();
	}
	
	@Override
	public Iterator<? extends E> iterator(final int first, final int count) {
		List<? extends E> list = listModel.getObject();
		
		int toIndex = first + count;
		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		return list.subList(first, toIndex).listIterator();
	}

	@Override
	public int size() {
		return listModel.getObject().size();
	}

	@Override
	public IModel<E> model(E object) {
		return new GenericEntityModel<K, E>(object);
	}

}
