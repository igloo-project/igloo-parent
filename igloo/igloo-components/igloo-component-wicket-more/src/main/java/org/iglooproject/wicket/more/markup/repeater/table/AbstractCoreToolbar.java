package org.iglooproject.wicket.more.markup.repeater.table;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class AbstractCoreToolbar extends Panel {
  private static final long serialVersionUID = 1L;

  private final CoreDataTable<?, ?> table;

  public AbstractCoreToolbar(final IModel<?> model, final CoreDataTable<?, ?> table) {
    super(table.newToolbarId(), model);
    this.table = table;
  }

  public AbstractCoreToolbar(final CoreDataTable<?, ?> table) {
    this(null, table);
  }

  protected CoreDataTable<?, ?> getTable() {
    return table;
  }
}
