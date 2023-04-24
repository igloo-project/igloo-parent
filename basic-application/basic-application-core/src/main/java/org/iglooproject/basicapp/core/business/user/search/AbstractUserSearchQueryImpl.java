package org.iglooproject.basicapp.core.business.user.search;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public abstract class AbstractUserSearchQueryImpl<U extends User> implements IAbstractUserSearchQuery<U> /* NOT Serializable */ {

	protected AbstractUserSearchQueryImpl(Class<? extends U> clazz) {
		//TODO: igloo-boot
//		super(clazz);
	}

	@Override
	public IAbstractUserSearchQuery<U> nameAutocomplete(String terms) {
//		must(matchAutocompleteIfGiven(terms, Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().username()));
		return this;
	}

	@Override
	public IAbstractUserSearchQuery<U> name(String name) {
//		must(matchFuzzyIfGiven(name, 2, Bindings.user().firstName(), Bindings.user().lastName(), Bindings.user().username()));
		return this;
	}

	@Override
	public IAbstractUserSearchQuery<U> group(UserGroup group) {
//		must(matchIfGiven(Bindings.user().groups().getPath(), group));
		return this;
	}

	@Override
	public IAbstractUserSearchQuery<U> enabled(EnabledFilter enabledFilter) {
//		if (enabledFilter != null && !EnabledFilter.ALL.equals(enabledFilter)) {
//			switch (enabledFilter) {
//			case ENABLED_ONLY:
//				must(matchIfGiven(User.ENABLED, Boolean.TRUE));
//				break;
//			case DISABLED_ONLY:
//				must(matchIfGiven(User.ENABLED, Boolean.FALSE));
//				break;
//			default:
//				throw new IllegalSwitchValueException(enabledFilter);
//			}
//		}
		return this;
	}

	@Override
	public ISearchQuery<U, UserSort> sort(Map<UserSort, SortOrder> sortMap) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public List<U> fullList() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public List<U> list(long offset, long limit) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

}