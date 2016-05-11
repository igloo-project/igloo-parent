package fr.openwide.core.wicket.more.bindable.model;

import java.util.Map;

import fr.openwide.core.wicket.more.markup.repeater.map.IItemModelAwareMapModel;

public interface IBindableMapModel<K, V, M extends Map<K, V>>
		extends IBindableModel<M>, IItemModelAwareMapModel<K, V, M, IBindableModel<K>, IBindableModel<V>> {

}
