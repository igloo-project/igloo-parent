package org.iglooproject.basicapp.web.application.common.component;

import com.google.common.collect.ImmutableList;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.markup.html.panel.GenericPanel;
import java.util.List;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;

public class EnvironmentPanel extends GenericPanel<Environment> {

  private static final long serialVersionUID = -916735857360352450L;

  private static final List<Environment> VISIBLE_ALERTS =
      ImmutableList.of(Environment.development, Environment.staging);

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
            || BasicApplicationSession.get().getUser() instanceof TechnicalUser);
  }
}
