package org.iglooproject.wicket.more.bindable.component;

import org.iglooproject.wicket.more.bindable.model.IBindableCollectionModel;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.iglooproject.wicket.more.markup.repeater.collection.SpecificModelCollectionView;

/**
 * An easy-to-use shorthand for {@code SpecificModelCollectionView<T, IBindableModel<T>>}.
 *
 * <p>This class is only useful if you want to use an {@link IBindableModel} for your item models.
 * If you want to use a custom subtype of yours, go with {@link SpecificModelCollectionView}
 * directly.
 *
 * @author yrodiere
 * @param <T>
 * @see SpecificModelCollectionView
 */
public abstract class BindableCollectionView<T>
    extends SpecificModelCollectionView<T, IBindableModel<T>> {

  private static final long serialVersionUID = 1L;

  public BindableCollectionView(String id, IBindableCollectionModel<T, ?> collectionModel) {
    super(id, collectionModel);
  }

  @Override
  @SuppressWarnings("unchecked")
  public IBindableCollectionModel<T, ?> getModel() {
    return (IBindableCollectionModel<T, ?>) super.getModel();
  }
}
