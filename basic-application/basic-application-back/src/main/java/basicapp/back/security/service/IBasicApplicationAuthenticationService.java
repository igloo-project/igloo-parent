package basicapp.back.security.service;

import org.iglooproject.jpa.security.service.IAuthenticationService;

import basicapp.back.business.user.model.User;

public interface IBasicApplicationAuthenticationService extends IAuthenticationService {

	User getUser();

}
