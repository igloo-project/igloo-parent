package fr.openwide.core.showcase.core.business.user.search;

import org.springframework.context.annotation.Scope;

import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.showcase.core.business.user.model.User;

@Scope("prototype")
public interface IGenericUserSearchQuery<U extends User> extends ISearchQuery<U, UserSort> {
	
	IGenericUserSearchQuery<U> name(String nameToMatch);

	IGenericUserSearchQuery<U> includeInactive(Boolean includeInactives);

	IGenericUserSearchQuery<U> nameAutocomplete(String terms);
	
}
