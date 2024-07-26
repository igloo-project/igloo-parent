package org.iglooproject.wicket.more.link.model;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.springframework.util.Assert;

public class ComponentPageModel implements IModel<Page> {

  private static final long serialVersionUID = 6402816198383449645L;

  private final Component component;

  public ComponentPageModel(Component component) {
    this.component = component;
    Assert.notNull(
        component, "[Assertion failed] - this argument is required; it must not be null");
  }

  @Override
  public Page getObject() {
    return component.getPage();
  }
}
