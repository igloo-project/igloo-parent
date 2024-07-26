package org.iglooproject.wicket.more.markup.repeater.table.builder.action.factory;

import igloo.wicket.action.IOneParameterAction;
import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public class ActionColumnActionFactory<T>
    implements IOneParameterComponentFactory<Link<T>, IModel<T>> {

  private static final long serialVersionUID = 1L;

  private final IOneParameterAction<? super IModel<T>> action;

  public ActionColumnActionFactory(IOneParameterAction<? super IModel<T>> action) {
    super();
    this.action = action;
  }

  @Override
  public Link<T> create(String wicketId, final IModel<T> parameter) {
    return new Link<T>(wicketId, parameter) {
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick() {
        action.execute(parameter);
      }
    };
  }

  @Override
  public void detach() {
    action.detach();
  }
}
