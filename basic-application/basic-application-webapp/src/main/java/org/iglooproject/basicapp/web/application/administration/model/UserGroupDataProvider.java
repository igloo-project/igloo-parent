package org.iglooproject.basicapp.web.application.administration.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.search.IUserGroupSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.UserGroupSort;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

import com.google.common.collect.ImmutableMap;

import igloo.wicket.model.Detachables;

public class UserGroupDataProvider extends AbstractSearchQueryDataProvider<UserGroup, UserGroupSort> {

	private static final long serialVersionUID = 7805366114079528005L;

	private final IModel<? extends User> userModel;

	private final IModel<String> nameModel = new Model<>();

	private final CompositeSortModel<UserGroupSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			UserGroupSort.NAME, UserGroupSort.NAME.getDefaultOrder(),
			UserGroupSort.ID, UserGroupSort.ID.getDefaultOrder()
		),
		ImmutableMap.of(
			UserGroupSort.ID, UserGroupSort.ID.getDefaultOrder()
		)
	);

	public UserGroupDataProvider(IModel<? extends User> userModel) {
		super();
		this.userModel = userModel;
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}
	
	@Override
	public IModel<UserGroup> model(UserGroup userGroup) {
		return GenericEntityModel.of(userGroup);
	}

	public CompositeSortModel<UserGroupSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected ISearchQuery<UserGroup, UserGroupSort> getSearchQuery() {
		return createSearchQuery(IUserGroupSearchQuery.class)
			.user(userModel.getObject())
			.name(nameModel.getObject())
			.sort(sortModel.getObject());
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			userModel,
			nameModel,
			sortModel
		);
	}

}
