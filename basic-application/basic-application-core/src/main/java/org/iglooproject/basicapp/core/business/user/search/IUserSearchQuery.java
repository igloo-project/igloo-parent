package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public interface IUserSearchQuery<U extends User> extends IAbstractUserSearchQuery<U> {}
