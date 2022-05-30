package org.iglooproject.wicket.more.model;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.IItemModelAwareCollectionModel;
import org.iglooproject.wicket.api.model.CollectionCopyModel;

public class WorkingCopyCollectionModel<T, C extends Collection<T>, M extends IModel<T>>
		extends WorkingCopyModel<C>
		implements IItemModelAwareCollectionModel<T, C, M> {
	
	private static final long serialVersionUID = -4049247716740595168L;

	private final IItemModelAwareCollectionModel<T, C, M> collectionWorkingCopy;
	
	public WorkingCopyCollectionModel(IModel<C> reference, CollectionCopyModel<T, C, M> workingCopy) {
		super(reference, workingCopy);
		this.collectionWorkingCopy = workingCopy;
	}

	@Override
	public C getObject() {
		return collectionWorkingCopy.getObject();
	}

	@Override
	public void setObject(C object) {
		collectionWorkingCopy.setObject(object);
	}
	
	@Override
	public Iterator<M> iterator(long offset, long limit) {
		return collectionWorkingCopy.iterator(offset, limit);
	}
	
	@Override
	public Iterator<M> iterator() {
		return collectionWorkingCopy.iterator();
	}
	
	@Override
	public long size() {
		return collectionWorkingCopy.size();
	}

	@Override
	public void add(T item) {
		collectionWorkingCopy.add(item);
	}
	
	@Override
	public void remove(T item) {
		collectionWorkingCopy.remove(item);
	}

	@Override
	public void clear() {
		collectionWorkingCopy.clear();
	}
}
