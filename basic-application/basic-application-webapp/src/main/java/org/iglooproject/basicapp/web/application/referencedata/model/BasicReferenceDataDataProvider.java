package org.iglooproject.basicapp.web.application.referencedata.model;

import java.util.function.UnaryOperator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSearchQueryData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSort;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

import com.google.common.collect.ImmutableMap;

import igloo.wicket.model.Detachables;
import igloo.wicket.spring.SpringBeanLookupCache;

public class BasicReferenceDataDataProvider<T extends ReferenceData<? super T>> extends SearchQueryDataProvider<T, BasicReferenceDataSort, BasicReferenceDataSearchQueryData<T>, IBasicReferenceDataSearchQuery<T>> {

	private static final long serialVersionUID = 1L;

	private final SpringBeanLookupCache<IBasicReferenceDataSearchQuery<T>> searchQueryLookupCache;

	private final CompositeSortModel<BasicReferenceDataSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			BasicReferenceDataSort.POSITION, BasicReferenceDataSort.POSITION.getDefaultOrder(),
			BasicReferenceDataSort.LABEL_FR, BasicReferenceDataSort.LABEL_FR.getDefaultOrder(),
			BasicReferenceDataSort.LABEL_EN, BasicReferenceDataSort.LABEL_EN.getDefaultOrder()
		),
		ImmutableMap.of(
			BasicReferenceDataSort.ID, BasicReferenceDataSort.ID.getDefaultOrder()
		)
	);

	public BasicReferenceDataDataProvider(Class<T> clazz) {
		this(clazz, UnaryOperator.identity());
	}

	public BasicReferenceDataDataProvider(Class<T> clazz, UnaryOperator<DataModel<BasicReferenceDataSearchQueryData<T>>> dataModelOperator) {
		this(
			clazz,
			dataModelOperator.apply(
				new DataModel<>(() -> new BasicReferenceDataSearchQueryData<T>())
					.bind(Bindings.basicReferenceDataSearchQueryData().label(), Model.of())
					.bind(Bindings.basicReferenceDataSearchQueryData().enabledFilter(), Model.of())
			)
		);
	}

	public BasicReferenceDataDataProvider(Class<T> clazz, IModel<BasicReferenceDataSearchQueryData<T>> dataModel) {
		super(dataModel);
		this.searchQueryLookupCache = SpringBeanLookupCache.<IBasicReferenceDataSearchQuery<T>>of(
			() -> CoreWicketApplication.get().getApplicationContext(),
			IBasicReferenceDataSearchQuery.class,
			clazz
		);
	}

	@Override
	public CompositeSortModel<BasicReferenceDataSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected IBasicReferenceDataSearchQuery<T> searchQuery() {
		return searchQueryLookupCache.get();
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(searchQueryLookupCache);
	}

}
