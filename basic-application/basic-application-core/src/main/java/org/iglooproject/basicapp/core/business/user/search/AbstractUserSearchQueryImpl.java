package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;

public abstract class AbstractUserSearchQueryImpl<U extends User> extends AbstractHibernateSearchSearchQuery<U, UserSort>
		implements IAbstractUserSearchQuery<U> /* NOT Serializable */ {

	protected AbstractUserSearchQueryImpl(Class<? extends U> clazz) {
		super(clazz);
	}

	@Override
	public IAbstractUserSearchQuery<U> nameAutocomplete(String terms) {
		must(matchAutocompleteIfGiven(terms, Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().username()));
		return this;
	}

	@Override
	public IAbstractUserSearchQuery<U> name(String name) {
		must(matchFuzzyIfGiven(name, 2, Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().username()));
		return this;
	}

	@Override
	public IAbstractUserSearchQuery<U> group(UserGroup group) {
		must(matchIfGiven(Bindings.user().groups().getPath(), group));
		return this;
	}

	@Override
	public IAbstractUserSearchQuery<U> enabled(EnabledFilter enabledFilter) {
		if (enabledFilter != null && !EnabledFilter.ALL.equals(enabledFilter)) {
			switch (enabledFilter) {
			case ENABLED_ONLY:
				must(matchIfGiven(User.ENABLED, Boolean.TRUE));
				break;
			case DISABLED_ONLY:
				must(matchIfGiven(User.ENABLED, Boolean.FALSE));
				break;
			default:
				throw new IllegalSwitchValueException(enabledFilter);
			}
		}
		return this;
	}

}