package igloo.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.javatuples.Pair;

public class SeparatorContainer extends WebMarkupContainer {

  private static final long serialVersionUID = 7764638101080488816L;

  private Pair<Component, Component> components;

  public SeparatorContainer(String id) {
    super(id);
  }

  public SeparatorContainer components(Component component1, Component component2) {
    components = new Pair<>(component1, component2);

    return this;
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();

    if (components != null) {
      components.getValue0().configure();
      components.getValue1().configure();

      setVisible(
          components.getValue0().determineVisibility()
              && components.getValue1().determineVisibility());
    } else {
      setVisible(false);
    }
  }
}
