package fr.openwide.core.wicket.more.model.threadsafe.impl;

public class LoadableDetachableModelThreadContext<T> {
	private boolean attached = false;
	private T transientModelObject = null;
	
	public boolean isAttached() {
		return attached;
	}
	
	/*package*/ void setAttached(boolean attached) {
		this.attached = attached;
	}
	
	public T getTransientModelObject() {
		return transientModelObject;
	}
	
	/*package*/ void setTransientModelObject(T transientModelObject) {
		this.transientModelObject = transientModelObject;
	}
}