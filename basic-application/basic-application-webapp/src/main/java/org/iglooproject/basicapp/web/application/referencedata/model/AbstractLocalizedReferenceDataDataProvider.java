package org.iglooproject.basicapp.web.application.referencedata.model;

import org.apache.lucene.search.SortField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.ILocalizedReferenceDataSearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.util.model.Detachables;

public abstract class AbstractLocalizedReferenceDataDataProvider<T extends LocalizedReferenceData<? super T>, S extends ISort<SortField>>
		extends AbstractGenericReferenceDataDataProvider<T, S> {

	private static final long serialVersionUID = -1391750059166474629L;

	private final IModel<String> labelModel = Model.of();

	private final IModel<String> codeModel = Model.of();

	protected AbstractLocalizedReferenceDataDataProvider(CompositeSortModel<S> sortModel) {
		super(sortModel);
	}

	@Override
	public ILocalizedReferenceDataSearchQuery<T, S, ?> getSearchQuery() {
		return ((ILocalizedReferenceDataSearchQuery<T, S, ?>) super.getSearchQuery())
			.label(labelModel.getObject())
			.code(codeModel.getObject());
	}

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	public IModel<String> getCodeModel() {
		return codeModel;
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(
			labelModel,
			codeModel
		);
	}

}
