package org.iglooproject.basicapp.web.application.referencedata.model;

import org.apache.lucene.search.SortField;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.IReferenceDataSearchQuery;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public abstract class AbstractReferenceDataDataProvider<T extends ReferenceData<? super T>, S extends ISort<SortField>>
		extends AbstractSearchQueryDataProvider<T, S> {

	private static final long serialVersionUID = -1391750059166474629L;

	private final IModel<String> labelModel = Model.of();

	private final IModel<String> codeModel = Model.of();

	private final IModel<EnabledFilter> enabledFilterModel = new Model<>(EnabledFilter.ENABLED_ONLY);

	private final CompositeSortModel<S> sortModel;

	protected AbstractReferenceDataDataProvider(CompositeSortModel<S> sortModel) {
		Injector.get().inject(this);
		this.sortModel = sortModel;
	}

	@Override
	public IModel<T> model(T item) {
		return GenericEntityModel.of(item);
	}

	protected abstract IReferenceDataSearchQuery<T, S, ?> createSearchQuery();

	@Override
	public IReferenceDataSearchQuery<T, S, ?> getSearchQuery() {
		return createSearchQuery()
			.label(labelModel.getObject())
			.code(codeModel.getObject())
			.enabled(enabledFilterModel.getObject())
			.sort(sortModel.getObject());
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	public IModel<String> getCodeModel() {
		return codeModel;
	}

	public IModel<EnabledFilter> getEnabledFilterModel() {
		return enabledFilterModel;
	}

	public CompositeSortModel<S> getSortModel() {
		return sortModel;
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			labelModel,
			codeModel,
			enabledFilterModel,
			sortModel
		);
	}

}
