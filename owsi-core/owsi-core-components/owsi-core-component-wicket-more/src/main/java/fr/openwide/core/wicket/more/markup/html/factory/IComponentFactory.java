package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

public interface IComponentFactory<C extends Component> extends IDetachable {
	
	C create(String wicketId);

}
