package fr.openwide.core.wicket.more.bindable.model;

import java.util.Collection;

import fr.openwide.core.wicket.more.markup.repeater.collection.ICollectionModel;
import fr.openwide.core.wicket.more.markup.repeater.collection.IItemModelAwareCollectionModel;

/**
 * A {@link IBindableModel} that also is a {@link ICollectionModel}.
 * 
 * @see IBindable#bindCollectionWithCache(org.bindgen.BindingRoot, com.google.common.base.Supplier, com.google.common.base.Function)
 * @see IBindable#bindCollectionAlreadyAdded(org.bindgen.BindingRoot)
 */
public interface IBindableCollectionModel<T, C extends Collection<T>>
		extends IBindableModel<C>, IItemModelAwareCollectionModel<T, C, IBindableModel<T>> {

}
