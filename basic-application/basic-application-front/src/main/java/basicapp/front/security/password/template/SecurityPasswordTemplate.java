package basicapp.front.security.password.template;

import basicapp.front.BasicApplicationSession;
import basicapp.front.common.template.ApplicationAccessTemplate;
import igloo.wicket.condition.Condition;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class SecurityPasswordTemplate extends ApplicationAccessTemplate {

  private static final long serialVersionUID = -4350860041946569108L;

  public SecurityPasswordTemplate(PageParameters parameters) {
    super(parameters);

    if (!keepSignedIn().applies() && BasicApplicationSession.get().isSignedIn()) {
      BasicApplicationSession.get().invalidate();
      throw new RestartResponseException(getClass(), parameters);
    }
  }

  public Condition keepSignedIn() {
    return Condition.alwaysTrue();
  }
}
