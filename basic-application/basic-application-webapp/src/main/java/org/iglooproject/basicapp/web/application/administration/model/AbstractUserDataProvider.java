package org.iglooproject.basicapp.web.application.administration.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.search.IAbstractUserSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.UserSort;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractUserDataProvider<U extends User> extends AbstractSearchQueryDataProvider<U, UserSort> {

	private static final long serialVersionUID = -8540890431031886412L;

	private final Class<U> clazz;

	private final IModel<String> nameModel = Model.of();

	private final IModel<UserGroup> groupModel = new GenericEntityModel<>();

	private final IModel<EnabledFilter> enabledFilterModel = new Model<>(EnabledFilter.ENABLED_ONLY);

	private final CompositeSortModel<UserSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			UserSort.LAST_NAME, UserSort.LAST_NAME.getDefaultOrder(),
			UserSort.FIRST_NAME, UserSort.FIRST_NAME.getDefaultOrder(),
			UserSort.ID, UserSort.ID.getDefaultOrder()
		),
		ImmutableMap.of(
			UserSort.ID, UserSort.ID.getDefaultOrder()
		)
	);

	public AbstractUserDataProvider(Class<U> clazz) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
	}

	@Override
	public IModel<U> model(U item) {
		return new GenericEntityModel<>(item);
	}

	public Class<U> getClazz() {
		return clazz;
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}

	public IModel<UserGroup> getGroupModel() {
		return groupModel;
	}

	public IModel<EnabledFilter> getEnabledFilterModel() {
		return enabledFilterModel;
	}

	public CompositeSortModel<UserSort> getSortModel() {
		return sortModel;
	}

	protected abstract IAbstractUserSearchQuery<U> createSearchQuery();

	@Override
	protected ISearchQuery<U, UserSort> getSearchQuery() {
		return createSearchQuery()
			.name(nameModel.getObject())
			.group(groupModel.getObject())
			.enabled(enabledFilterModel.getObject())
			.sort(sortModel.getObject());
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			nameModel,
			groupModel,
			enabledFilterModel,
			sortModel
		);
	}

}
