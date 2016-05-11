package fr.openwide.core.wicket.more.bindable.model;

import java.util.Collection;

import fr.openwide.core.wicket.more.markup.repeater.collection.IItemModelAwareCollectionModel;

public interface IBindableCollectionModel<T, C extends Collection<T>>
		extends IBindableModel<C>, IItemModelAwareCollectionModel<T, C, IBindableModel<T>> {

}
