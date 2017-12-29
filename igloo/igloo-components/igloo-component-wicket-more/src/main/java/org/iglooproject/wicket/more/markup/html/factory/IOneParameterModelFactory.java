package org.iglooproject.wicket.more.markup.html.factory;

import org.apache.wicket.model.IModel;

/**
 * @deprecated Use {@link IDetachableFactory IOneParameterFactory&lt;T, IModel&lt;U&gt;&gt;} instead.
 */
@Deprecated
public interface IOneParameterModelFactory<T, U> extends IDetachableFactory<T, IModel<U>> {

}
