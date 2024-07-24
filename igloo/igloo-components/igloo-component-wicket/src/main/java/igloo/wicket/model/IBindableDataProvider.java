package igloo.wicket.model;

import org.bindgen.Bindable;

/**
 * Lorsqu'elle est implémentée, cette interface permet d'accéder à la propriété "size" d'un
 * DataProvider par le biais des bindings.
 */
@Bindable
public interface IBindableDataProvider {

  long size();
}
