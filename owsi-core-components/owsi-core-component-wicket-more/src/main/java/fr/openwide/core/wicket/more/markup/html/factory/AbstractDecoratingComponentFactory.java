package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;

public abstract class AbstractDecoratingComponentFactory<C extends Component> extends AbstractComponentFactory<C> {
	
	private static final long serialVersionUID = -6411443989774223672L;
	
	private final IComponentFactory<? extends C> delegate;

	public AbstractDecoratingComponentFactory(IComponentFactory<? extends C> delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public C create(String wicketId) {
		C result = delegate.create(wicketId);
		return decorate(result);
	}

	protected abstract C decorate(C component);

	@Override
	public void detach() {
		delegate.detach();
	}

}
