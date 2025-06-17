package basicapp.front.common.component;

import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.back.config.util.Environment;
import basicapp.front.BasicApplicationSession;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.markup.html.panel.GenericPanel;
import java.util.List;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.model.IModel;

public class EnvironmentPanel extends GenericPanel<Environment> {

  private static final long serialVersionUID = -916735857360352450L;

  private static final List<Environment> VISIBLE_ALERTS =
      List.of(
          Environment.development,
          Environment.integration,
          Environment.qualification,
          Environment.preproduction);

  public EnvironmentPanel(String id) {
    this(id, BasicApplicationSession.get().getEnvironmentModel());
  }

  public EnvironmentPanel(String id, final IModel<Environment> environmentModel) {
    super(id, environmentModel);
    setOutputMarkupId(true);

    add(new EnumLabel<>("environment", environmentModel));
    add(new ClassAttributeAppender(() -> "environment-section-" + environmentModel.getObject()));
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();

    setVisible(
        VISIBLE_ALERTS.contains(getModelObject())
            || UserPredicates.technical().apply(BasicApplicationSession.get().getUser()));
  }
}
