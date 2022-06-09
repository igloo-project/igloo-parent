package org.iglooproject.wicket.more.bindable.model;

import java.util.Collection;

import igloo.wicket.model.ICollectionModel;
import igloo.wicket.model.IItemModelAwareCollectionModel;

/**
 * A {@link IBindableModel} that also is a {@link ICollectionModel}.
 * 
 * @see IBindableModel#bindCollectionWithCache(org.bindgen.BindingRoot, com.google.common.base.Supplier, com.google.common.base.Function)
 * @see IBindableModel#bindCollectionAlreadyAdded(org.bindgen.BindingRoot)
 */
public interface IBindableCollectionModel<T, C extends Collection<T>>
		extends IBindableModel<C>, IItemModelAwareCollectionModel<T, C, IBindableModel<T>> {

}
