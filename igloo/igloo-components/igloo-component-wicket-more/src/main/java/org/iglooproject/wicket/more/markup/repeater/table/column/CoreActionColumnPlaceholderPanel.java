package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class CoreActionColumnPlaceholderPanel<T> extends Panel {

  private static final long serialVersionUID = 1L;

  public CoreActionColumnPlaceholderPanel(String id, final IModel<T> rowModel) {
    super(id);

    add(getComponent("placeholder", rowModel));
  }

  protected abstract Component getComponent(String wicketId, IModel<T> rowModel);
}
