package org.iglooproject.basicapp.web.application;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.ENVIRONMENT;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;

public class BasicApplicationSession extends AbstractCoreSession<User> {

  private static final long serialVersionUID = 1870827020904365541L;

  private final IModel<Environment> environmentModel = ApplicationPropertyModel.of(ENVIRONMENT);

  public BasicApplicationSession(Request request) {
    super(request);
  }

  public static BasicApplicationSession get() {
    return (BasicApplicationSession) Session.get();
  }

  public IModel<Environment> getEnvironmentModel() {
    return environmentModel;
  }
}
