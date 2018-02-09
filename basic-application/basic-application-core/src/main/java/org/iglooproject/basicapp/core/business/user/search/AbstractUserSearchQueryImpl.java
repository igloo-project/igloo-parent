package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;

public abstract class AbstractUserSearchQueryImpl<U extends User> extends AbstractHibernateSearchSearchQuery<U, UserSort>
		implements IGenericUserSearchQuery<U> /* NOT Serializable */ {

	protected AbstractUserSearchQueryImpl(Class<? extends U> clazz) {
		super(clazz);
	}

	@Override
	public IGenericUserSearchQuery<U> name(String name) {
		must(matchFuzzyIfGiven(name, 2,
				Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().username()));
		return this;
	}
	
	@Override
	public IGenericUserSearchQuery<U> group(UserGroup group) {
		must(matchIfGiven(Bindings.user().groups().getPath(), group));
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
				Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().username()));
		return this;
	}
}