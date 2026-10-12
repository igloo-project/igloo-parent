package basicapp.front.security.password.template;

import basicapp.front.common.template.ApplicationAccessTemplate;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class SecurityPasswordTemplate extends ApplicationAccessTemplate {

  private static final long serialVersionUID = -4350860041946569108L;

  protected SecurityPasswordTemplate(PageParameters parameters) {
    super(parameters);
  }
}
