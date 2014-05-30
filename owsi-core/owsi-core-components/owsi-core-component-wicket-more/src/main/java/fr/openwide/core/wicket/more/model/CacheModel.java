package fr.openwide.core.wicket.more.model;

import org.apache.wicket.model.IModel;

public class CacheModel<T> implements IModel<T> {
	
	private static final long serialVersionUID = -4049247716740595168L;

	private final IModel<T> reference;
	private final IModel<T> cache;
	
	public CacheModel(IModel<T> reference, IModel<T> cache) {
		this.reference = reference;
		this.cache = cache;
		read();
	}

	@Override
	public void detach() {
		this.reference.detach();
		this.cache.detach();
	}

	@Override
	public T getObject() {
		return cache.getObject();
	}

	@Override
	public void setObject(T object) {
		reference.setObject(object);
		read();
	}

	public void read() {
		cache.setObject(reference.getObject());
	}

	public IModel<T> getReferenceModel() {
		return reference;
	}
}
