package igloo.wicket.component;

import igloo.wicket.condition.AbstractConfigurableComponentBooleanPropertyBehavior.Operator;
import igloo.wicket.condition.AbstractConfigurableComponentBooleanPropertyContainer;
import igloo.wicket.condition.ComponentBooleanProperty;
import igloo.wicket.condition.ComponentBooleanPropertyBehavior;

public class EnclosureContainer
    extends AbstractConfigurableComponentBooleanPropertyContainer<EnclosureContainer> {

  private static final long serialVersionUID = 8163938380844150417L;

  public EnclosureContainer(String id) {
    this(id, ComponentBooleanProperty.VISIBLE);
  }

  public EnclosureContainer(String id, ComponentBooleanProperty property) {
    super(id, new ComponentBooleanPropertyBehavior(property, Operator.WHEN_ANY_TRUE));
  }

  @Override
  protected EnclosureContainer thisAsT() {
    return this;
  }
}
