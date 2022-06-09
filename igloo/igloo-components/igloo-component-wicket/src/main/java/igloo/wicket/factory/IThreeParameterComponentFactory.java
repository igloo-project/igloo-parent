package igloo.wicket.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

public interface IThreeParameterComponentFactory<C extends Component, P1, P2, P3> extends IDetachable {

	C create(String wicketId, P1 parameter1, P2 parameter2, P3 parameter3);

	@Override
	default void detach() {
	}

}
