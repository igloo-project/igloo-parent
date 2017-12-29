package org.iglooproject.wicket.more.markup.html.factory;


public abstract class AbstractDetachableFactory<T, R> implements IDetachableFactory<T, R> {

	private static final long serialVersionUID = -1691019680406494115L;

	@Override
	public void detach() {
		// nothing to do
	}

}
