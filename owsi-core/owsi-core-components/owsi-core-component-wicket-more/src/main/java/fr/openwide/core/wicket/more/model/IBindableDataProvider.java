package fr.openwide.core.wicket.more.model;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.bindgen.Bindable;

/**
 * Lorsqu'elle est implémentée, cette interface permet d'accéder à la propriété "size" d'un DataProvider par le biais des bindings.
 */
@Bindable
public interface IBindableDataProvider<T> extends IDataProvider<T> {

}
