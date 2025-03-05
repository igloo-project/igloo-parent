package basicapp.front;

import static basicapp.back.property.BasicApplicationCorePropertyIds.ENVIRONMENT;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import basicapp.back.config.util.Environment;
import java.util.Locale;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicApplicationSession extends AbstractCoreSession<User> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(BasicApplicationSession.class);

  @SpringBean private IUserService userService;

  private final IModel<Environment> environmentModel = ApplicationPropertyModel.of(ENVIRONMENT);

  public BasicApplicationSession(Request request) {
    super(request);
  }

  public static BasicApplicationSession get() {
    return (BasicApplicationSession) Session.get();
  }

  @Override
  protected void onSignIn(User user) {
    try {
      userService.updateLastLoginDate(user);

      Locale locale = user.getLocale();
      if (locale != null) {
        setLocale(user.getLocale());
      } else {
        userService.updateLocale(user, getLocale());
      }
    } catch (Exception e) {
      LOGGER.error(
          String.format("Unable to update the user information on sign in: %1$s", user), e);
    }
  }

  public IModel<Environment> getEnvironmentModel() {
    return environmentModel;
  }
}
