package igloo.bootstrap.jsmodel;

import java.util.Collection;

import org.apache.wicket.model.IDetachable;

public interface IJsDetachable {

	Collection<IDetachable> getDetachables();

}
