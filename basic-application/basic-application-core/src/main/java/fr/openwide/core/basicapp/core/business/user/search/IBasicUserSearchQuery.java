package fr.openwide.core.basicapp.core.business.user.search;

import org.springframework.context.annotation.Scope;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;

@Scope("prototype")
public interface IBasicUserSearchQuery extends IGenericUserSearchQuery<BasicUser> {
	
}
