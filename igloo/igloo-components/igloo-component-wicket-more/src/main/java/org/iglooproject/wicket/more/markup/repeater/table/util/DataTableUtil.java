package org.iglooproject.wicket.more.markup.repeater.table.util;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;

public final class DataTableUtil {

  private DataTableUtil() {}

  public static <E> MarkupContainer getRowItem(Item<ICellPopulator<E>> cellItem) {
    if (cellItem != null) {
      // cellItem.getParent() => RepeatingView
      return cellItem.getParent().getParent();
    }
    return null;
  }
}
