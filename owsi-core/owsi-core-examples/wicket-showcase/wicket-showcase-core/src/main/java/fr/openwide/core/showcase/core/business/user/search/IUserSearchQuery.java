package fr.openwide.core.showcase.core.business.user.search;

import org.springframework.context.annotation.Scope;

import fr.openwide.core.showcase.core.business.user.model.User;

@Scope("prototype")
public interface IUserSearchQuery extends IGenericUserSearchQuery<User> {
}
