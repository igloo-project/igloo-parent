package fr.openwide.core.showcase.core.business.user.search;

import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.util.binding.Bindings;

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