package org.iglooproject.wicket.more.markup.html.collection;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.model.ListItemModel;

public abstract class IndexedItemListView<T>
    extends AbstractPageableView<
        T> { // Does not implement IGenericComponent<C> in order to allow using IModel<? extends
  // List<T>>, not only IModel<List<T>>

  private static final long serialVersionUID = 6410475416792396400L;

  public IndexedItemListView(String id, IModel<? extends List<T>> model) {
    super(id, model);
  }

  @SuppressWarnings("unchecked")
  public IModel<? extends List<T>> getModel() {
    return (IModel<List<T>>) getDefaultModel();
  }

  @SuppressWarnings("unchecked")
  public List<T> getModelObject() {
    return (List<T>) getDefaultModelObject();
  }

  private ListItemModel<T> newItemModel(int index) {
    return new ListItemModel<>(getModel(), index);
  }

  @Override
  protected Iterator<IModel<T>> getItemModels(final long offset, final long size) {
    final List<T> list = getModelObject();
    if (list == null) {
      return Collections.emptyIterator();
    }

    return new UnmodifiableIterator<IModel<T>>() {
      private int index = (int) offset;
      private final int end = (int) (offset + size);

      @Override
      public boolean hasNext() {
        return index < end && index < internalGetItemCount();
      }

      @Override
      public IModel<T> next() {
        if (!hasNext()) {
          throw new NoSuchElementException(String.format("No item for index %d", index));
        }
        return newItemModel(index++);
      }
    };
  }

  @Override
  protected long internalGetItemCount() {
    return getModelObject().size();
  }

  /**
   * Useful when using a {@link ReuseIfModelsEqualStrategy} : removes the item from the underlying
   * model AND the view and reorganizes the other items so that their respective indices are
   * up-to-date.
   */
  public final void removeItem(Item<T> item) {
    removeItemFromView(item);
    removeItemFromModel(item);
  }

  /**
   * Useful when using a {@link ReuseIfModelsEqualStrategy} : removes the item from the view (not
   * the model) and reorganizes the other items so that their respective indices are up-to-date.
   */
  protected final void removeItemFromView(Item<T> item) {
    item.remove();
    for (Component child : this) {
      @SuppressWarnings("unchecked")
      Item<T> childItem = (Item<T>) child;
      int childIndex = childItem.getIndex();
      if (childIndex > item.getIndex()) {
        --childIndex;
        childItem.setIndex(childIndex);
        ((ListItemModel<?>) childItem.getModel()).getIndexModel().setObject(childIndex);
      }
    }
  }

  protected void removeItemFromModel(Item<T> item) {
    getModelObject().remove(item.getIndex());
  }
}
