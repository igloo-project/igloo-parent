package basicapp.front.common.form.impl;

import basicapp.back.business.user.model.User;
import basicapp.front.common.renderer.UserRenderer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.form.AbstractGenericEntityChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.form.GenericEntityRendererToChoiceRenderer;

public abstract class UserChoiceRenderer extends ChoiceRenderer<User> {

  private static final long serialVersionUID = -4657800061123148721L;

  private static final AbstractGenericEntityChoiceRenderer<User> INSTANCE =
      GenericEntityRendererToChoiceRenderer.of(UserRenderer.get());

  public static AbstractGenericEntityChoiceRenderer<User> get() {
    return INSTANCE;
  }

  private UserChoiceRenderer() {}
}
