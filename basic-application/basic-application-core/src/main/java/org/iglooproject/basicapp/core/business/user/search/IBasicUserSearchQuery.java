package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public interface IBasicUserSearchQuery extends IAbstractUserSearchQuery<BasicUser> {}
