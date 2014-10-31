package fr.openwide.core.basicapp.core.security.service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.security.model.SecurityOptions;

public interface ISecurityOptionsService {

	ISecurityOptionsService setOptions(Class<? extends User> clazz, SecurityOptions options);

	ISecurityOptionsService setDefaultOptions(SecurityOptions options);

	SecurityOptions getOptions(Class<? extends User> clazz);

}
