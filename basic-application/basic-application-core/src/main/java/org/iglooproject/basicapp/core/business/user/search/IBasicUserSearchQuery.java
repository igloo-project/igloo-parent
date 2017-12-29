package org.iglooproject.basicapp.core.business.user.search;

import org.springframework.context.annotation.Scope;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;

@Scope("prototype")
public interface IBasicUserSearchQuery extends IGenericUserSearchQuery<BasicUser> {
	
}
