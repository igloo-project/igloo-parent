package org.iglooproject.wicket.more.markup.repeater.table.column;

import igloo.wicket.model.BindingModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;

public class CoreBooleanLabelColumn<T, S extends ISort<?>> extends AbstractCoreColumn<T, S> {

  private static final long serialVersionUID = -5344972073351010752L;

  private final BindingRoot<? super T, Boolean> binding;

  private boolean hideIfNullOrFalse = false;

  public CoreBooleanLabelColumn(
      IModel<?> headerLabelModel, final BindingRoot<? super T, Boolean> binding) {
    super(headerLabelModel);
    this.binding = binding;
  }

  @Override
  public void populateItem(
      Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
    cellItem.add(
        new CoreBooleanLabelColumnPanel<S>(componentId, BindingModel.of(rowModel, binding)) {
          private static final long serialVersionUID = 1L;

          @Override
          protected BooleanIcon decorate(BooleanIcon booleanIcon) {
            return CoreBooleanLabelColumn.this.decorate(booleanIcon);
          }
        });
  }

  public BooleanIcon decorate(BooleanIcon booleanIcon) {
    if (hideIfNullOrFalse) {
      booleanIcon.hideIfNullOrFalse();
    }
    return booleanIcon;
  }

  public CoreBooleanLabelColumn<T, S> hideIfNullOrFalse() {
    this.hideIfNullOrFalse = true;
    return this;
  }
}
