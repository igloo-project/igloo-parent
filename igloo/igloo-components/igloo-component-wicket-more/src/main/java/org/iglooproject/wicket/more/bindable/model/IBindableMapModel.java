package org.iglooproject.wicket.more.bindable.model;

import igloo.wicket.model.IItemModelAwareMapModel;
import igloo.wicket.model.IMapModel;
import java.util.Map;

/**
 * A {@link IBindableModel} that also is a {@link IMapModel}.
 *
 * @see IBindableModel#bindMapWithCache(org.bindgen.BindingRoot, com.google.common.base.Supplier,
 *     com.google.common.base.Function, com.google.common.base.Function)
 * @see IBindableModel#bindMapAlreadyAdded(org.bindgen.BindingRoot)
 */
public interface IBindableMapModel<K, V, M extends Map<K, V>>
    extends IBindableModel<M>,
        IItemModelAwareMapModel<K, V, M, IBindableModel<K>, IBindableModel<V>> {}
