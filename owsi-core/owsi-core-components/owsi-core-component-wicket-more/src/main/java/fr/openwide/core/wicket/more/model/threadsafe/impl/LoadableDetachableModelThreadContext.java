package fr.openwide.core.wicket.more.model.threadsafe.impl;

public class LoadableDetachableModelThreadContext<T> {
	private T transientModelObject = null;
	
	public T getTransientModelObject() {
		return transientModelObject;
	}
	
	/*package*/ void setTransientModelObject(T transientModelObject) {
		this.transientModelObject = transientModelObject;
	}
}