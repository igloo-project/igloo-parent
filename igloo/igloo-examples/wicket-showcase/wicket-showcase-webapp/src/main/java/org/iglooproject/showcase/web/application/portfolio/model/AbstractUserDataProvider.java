package org.iglooproject.showcase.web.application.portfolio.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableMap;

import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.search.IGenericUserSearchQuery;
import org.iglooproject.showcase.core.business.user.search.UserSort;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public abstract class AbstractUserDataProvider<U extends User> extends AbstractSearchQueryDataProvider<U, UserSort> {
	
	private static final long serialVersionUID = -8540890431031886412L;
	
	private final Class<U> clazz;

	private final IModel<String> nameModel = Model.of();
	
	private final IModel<Boolean> includeInactivesModel = Model.of(Boolean.FALSE);
	
	private final CompositeSortModel<UserSort> sortModel = new CompositeSortModel<UserSort>(
			CompositingStrategy.LAST_ONLY,
			ImmutableMap.of(
					UserSort.FULL_NAME, UserSort.FULL_NAME.getDefaultOrder(),
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
		return new GenericEntityModel<Long, U>(item);
	}
	
	public Class<U> getClazz() {
		return clazz;
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}
	
	public IModel<Boolean> getIncludeInactivesModel() {
		return includeInactivesModel;
	}
	
	public CompositeSortModel<UserSort> getSortModel() {
		return sortModel;
	}
	
	protected abstract IGenericUserSearchQuery<U> createSearchQuery();
	
	@Override
	protected ISearchQuery<U, UserSort> getSearchQuery() {
		return createSearchQuery()
				.name(nameModel.getObject())
				.includeInactive(includeInactivesModel.getObject())
				.sort(sortModel.getObject());
	}
	
	@Override
	public void detach() {
		super.detach();
		nameModel.detach();
		sortModel.detach();
		includeInactivesModel.detach();
	}
}
