package igloo.bootstrap.tab;

import igloo.bootstrap.BootstrapRequestCycle;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;

public class BootstrapTabBehavior extends Behavior {

  private static final long serialVersionUID = 1645525017124363380L;

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    BootstrapRequestCycle.getSettings().tabRenderHead(component, response);
  }
}
