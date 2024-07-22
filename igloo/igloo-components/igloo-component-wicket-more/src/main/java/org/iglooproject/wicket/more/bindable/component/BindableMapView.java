package org.iglooproject.wicket.more.bindable.component;

import org.iglooproject.wicket.more.bindable.model.IBindableMapModel;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.iglooproject.wicket.more.markup.repeater.map.SpecificModelMapView;

/**
 * An easy-to-use shorthand for {@code SpecificModelMapView<T, IBindableModel<T>>}.
 *
 * <p>This class is only useful if you want to use an {@link IBindableModel} for your key models or
 * your value models. If you want to use a custom subtype of yours, go with {@link
 * SpecificModelMapView} directly.
 *
 * @author yrodiere
 * @param <T> The item type
 * @see SpecificModelMapView
 */
public abstract class BindableMapView<K, V>
    extends SpecificModelMapView<K, V, IBindableModel<K>, IBindableModel<V>> {

  private static final long serialVersionUID = 1L;

  public BindableMapView(String id, IBindableMapModel<K, V, ?> mapModel) {
    super(id, mapModel);
  }

  @Override
  @SuppressWarnings("unchecked")
  public IBindableMapModel<K, V, ?> getModel() {
    return (IBindableMapModel<K, V, ?>) super.getModel();
  }
}
