package org.iglooproject.basicapp.core.business.user.search;

import org.springframework.context.annotation.Scope;

import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;

@Scope("prototype")
public interface ITechnicalUserSearchQuery extends IGenericUserSearchQuery<TechnicalUser> {

}
