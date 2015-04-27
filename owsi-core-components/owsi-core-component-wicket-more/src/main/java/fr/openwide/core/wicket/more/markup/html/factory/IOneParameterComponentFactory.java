package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

public interface IOneParameterComponentFactory<C extends Component, P> extends IDetachable {
	
	C create(String wicketId, P parameter);
	
}