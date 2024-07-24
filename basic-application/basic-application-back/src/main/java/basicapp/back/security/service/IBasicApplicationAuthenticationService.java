package basicapp.back.security.service;

import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.IAuthenticationService;

public interface IBasicApplicationAuthenticationService extends IAuthenticationService {

  User getUser();
}
