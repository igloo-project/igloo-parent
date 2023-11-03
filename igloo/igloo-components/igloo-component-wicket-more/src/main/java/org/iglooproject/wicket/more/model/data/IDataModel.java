package org.iglooproject.wicket.more.model.data;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

public interface IDataModel<T> extends IModel<T> {

	<V> IDataModel<T> bind(BindingRoot<? super T, V> binding, IModel<V> model);

}
