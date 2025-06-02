package basicapp.front.notification.component;

import basicapp.back.config.util.Environment;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import java.util.List;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EnvironmentContainerPanel extends Panel {

  private static final long serialVersionUID = 1L;

  private static final List<Environment> ENVIRONMENTS_SHOW =
      List.of(
          Environment.development,
          Environment.integration,
          Environment.qualification,
          Environment.preproduction);

  public EnvironmentContainerPanel(String id, IModel<Environment> environmentModel) {
    super(id);

    add(
        new WebMarkupContainer("environment")
            .add(new EnumLabel<>("environment", environmentModel))
            .add(
                new ClassAttributeAppender(
                    () -> "header-section-environment-" + environmentModel.getObject())));

    add(Condition.contains(() -> ENVIRONMENTS_SHOW, environmentModel).thenShow());
  }
}
