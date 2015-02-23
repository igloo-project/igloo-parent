package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;

public abstract class AbstractComponentFactory<C extends Component> implements IComponentFactory<C> {

	private static final long serialVersionUID = 2083576320539676714L;

	@Override
	public abstract C create(String wicketId);

	@Override
	public void detach() {
		// Ne fait rien par défaut
	}

}
