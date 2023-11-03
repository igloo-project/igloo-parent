package org.iglooproject.basicapp.web.application.history.model;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.search.HistoryLogSearchQueryData;
import org.iglooproject.basicapp.core.business.history.search.IHistoryLogSearchQuery;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

import com.google.common.collect.ImmutableMap;

import igloo.wicket.model.CollectionCopyModel;

public class HistoryLogDataProvider extends SearchQueryDataProvider<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData, IHistoryLogSearchQuery> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private IHistoryLogSearchQuery searchQuery;

	private final CompositeSortModel<HistoryLogSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			HistoryLogSort.DATE, HistoryLogSort.DATE.getDefaultOrder()
		),
		ImmutableMap.of(
			HistoryLogSort.ID, HistoryLogSort.ID.getDefaultOrder()
		)
	);

	public HistoryLogDataProvider() {
		super(
			new DataModel<>(HistoryLogSearchQueryData::new)
				.bind(Bindings.historyLogSearchQueryData().subject(), new GenericEntityModel<>())
				.bind(Bindings.historyLogSearchQueryData().dateMin(), Model.of())
				.bind(Bindings.historyLogSearchQueryData().dateMax(), Model.of())
				.bind(Bindings.historyLogSearchQueryData().object(), new GenericEntityModel<>())
				.bind(Bindings.historyLogSearchQueryData().mandatoryDifferencesEventTypes(), CollectionCopyModel.serializable(Suppliers2.linkedHashSet()))
		);
	}

	@Override
	public CompositeSortModel<HistoryLogSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected IHistoryLogSearchQuery searchQuery() {
		return searchQuery;
	}

}
