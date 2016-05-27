package fr.openwide.core.wicket.more.markup.html.factory;

/**
 * @deprecated Use {@link AbstractDetachableFactory AbstractDetachableFactory&lt;T, IModel&lt;R&gt;&gt;} instead.
 */
@Deprecated
public abstract class AbstractOneParameterModelFactory<T, U> implements IOneParameterModelFactory<T, U> {

	private static final long serialVersionUID = -1691019680406494115L;

	@Override
	public void detach() {
		// nothing to do
	}

}
