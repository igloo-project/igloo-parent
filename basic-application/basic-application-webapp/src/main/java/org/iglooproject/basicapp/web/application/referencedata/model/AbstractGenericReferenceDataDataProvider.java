package org.iglooproject.basicapp.web.application.referencedata.model;

import org.apache.lucene.search.SortField;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.search.IGenericReferenceDataSearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.model.Detachables;

public abstract class AbstractGenericReferenceDataDataProvider<T extends GenericReferenceData<? super T, ?>, S extends ISort<SortField>>
		extends AbstractSearchQueryDataProvider<T, S> {

	private static final long serialVersionUID = 268769371721921631L;
	
	private final IModel<EnabledFilter> enabledFilterModel = new Model<>(EnabledFilter.ENABLED_ONLY);
	
	private final CompositeSortModel<S> sortModel;
	
	public AbstractGenericReferenceDataDataProvider(CompositeSortModel<S> sortModel) {
		this.sortModel = sortModel;
		Injector.get().inject(this);
	}
	
	@Override
	public IModel<T> model(T item) {
		return GenericEntityModel.of(item);
	}

	public IModel<EnabledFilter> getEnabledFilterModel() {
		return enabledFilterModel;
	}

	public CompositeSortModel<S> getSortModel() {
		return sortModel;
	}

	@Override
	public IGenericReferenceDataSearchQuery<T, S, ?> getSearchQuery() {
		return createSearchQuery()
			.enabled(getEnabledFilterModel().getObject())
			.sort(getSortModel().getObject());
	}

	protected abstract IGenericReferenceDataSearchQuery<T, S, ?> createSearchQuery();

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			enabledFilterModel,
			sortModel
		);
	}

}
