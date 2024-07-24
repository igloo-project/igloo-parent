package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public final class DefaultResetter implements Resetter {

  private static final long serialVersionUID = -4019437091633343436L;

  @Override
  public void reset(Component component) {
    IModel<?> model = component.getDefaultModel();

    // It is not necessary to set the model object to null if it already is.
    // Furthermore, in case of a PropertyModel it can cause a WicketRuntimeException if its
    // innermost object is null.
    if (model != null && model.getObject() != null) {
      model.setObject(null);
    }
  }
}
