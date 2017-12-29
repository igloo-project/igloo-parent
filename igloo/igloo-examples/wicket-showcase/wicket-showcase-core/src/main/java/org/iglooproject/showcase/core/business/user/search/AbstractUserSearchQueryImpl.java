package org.iglooproject.showcase.core.business.user.search;

import org.iglooproject.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.util.binding.Bindings;

public abstract class AbstractUserSearchQueryImpl<U extends User> extends AbstractHibernateSearchSearchQuery<U, UserSort>
		implements IGenericUserSearchQuery<U> /* NOT Serializable */ {

	
	protected AbstractUserSearchQueryImpl(Class<U> clazz) {
		super(clazz);
	}

	@Override
	public IGenericUserSearchQuery<U> name(String name) {
		must(matchFuzzyIfGiven(name, 2,
				Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().userName()));
		return this;
	}
	
	@Override
	public IGenericUserSearchQuery<U> includeInactive(Boolean includeInactives) {
		must(matchIfTrue(Bindings.user().active(), Boolean.TRUE, !includeInactives));
		return this;
	}
	
	@Override
	public IGenericUserSearchQuery<U> nameAutocomplete(String terms) {
		must(matchAutocompleteIfGiven(terms,
				Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().userName()));
		return this;
	}
}