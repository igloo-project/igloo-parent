package org.iglooproject.wicket.more;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityModel;

/**
 * {@link AbstractCoreAuthenticatedSession} whose signed in user is an entity, reloaded from the
 * database through a {@link SessionThreadSafeGenericEntityModel}.
 */
public abstract class AbstractCoreSession<U extends GenericEntity<Long, U> & IUser>
    extends AbstractCoreAuthenticatedSession<U> {

  private static final long serialVersionUID = 2591467597835056981L;

  @SpringBean(name = "userService")
  private ICoreUserSecurityService<U> userService;

  protected AbstractCoreSession(Request request) {
    super(request, new SessionThreadSafeGenericEntityModel<>());
  }

  /**
   * Use only if the application session is an entity session. Code that must work whatever the kind
   * of user (igloo components, console...) must use {@link AbstractCoreAuthenticatedSession#get()}.
   */
  public static AbstractCoreSession<?> get() {
    return (AbstractCoreSession<?>) Session.get();
  }

  @Override
  protected U loadUserByUsername(String username) {
    return userService.getByUsername(username);
  }
}
