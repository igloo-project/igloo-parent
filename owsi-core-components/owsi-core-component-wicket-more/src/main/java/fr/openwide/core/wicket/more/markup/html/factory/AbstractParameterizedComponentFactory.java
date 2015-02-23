package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;

public abstract class AbstractParameterizedComponentFactory<C extends Component, P> implements IParameterizedComponentFactory<C, P> {

	private static final long serialVersionUID = -8458161399798206102L;

	@Override
	public abstract C create(String wicketId, P parameter);

	@Override
	public void detach() {
		// Ne fait rien par défaut
	}

}
