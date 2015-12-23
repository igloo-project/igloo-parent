package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public interface IOneParameterModelFactory<T, U> extends IDetachable {

	IModel<U> create(T parameter);

}
