package org.iglooproject.basicapp.web.application.history.model;

import com.google.common.collect.ImmutableMap;
import igloo.wicket.model.CollectionCopyModel;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.search.HistoryLogSearchQueryData;
import org.iglooproject.basicapp.core.business.history.search.HistoryLogSort;
import org.iglooproject.basicapp.core.business.history.search.IHistoryLogSearchQuery;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

public class HistoryLogDataProvider
    extends SearchQueryDataProvider<
        HistoryLog, HistoryLogSort, HistoryLogSearchQueryData, IHistoryLogSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IHistoryLogSearchQuery searchQuery;

  private final CompositeSortModel<HistoryLogSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(HistoryLogSort.DATE, HistoryLogSort.DATE.getDefaultOrder().reverse()),
          ImmutableMap.of(HistoryLogSort.ID, HistoryLogSort.ID.getDefaultOrder()));

  public HistoryLogDataProvider() {
    this(UnaryOperator.identity());
  }

  public HistoryLogDataProvider(
      UnaryOperator<DataModel<HistoryLogSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(HistoryLogSearchQueryData::new)
                .bind(Bindings.historyLogSearchQueryData().dateMin(), Model.of())
                .bind(Bindings.historyLogSearchQueryData().dateMax(), Model.of())
                .bind(
                    Bindings.historyLogSearchQueryData().eventTypes(),
                    CollectionCopyModel.serializable(Suppliers2.linkedHashSet()))
                .bind(Bindings.historyLogSearchQueryData().subject(), new GenericEntityModel<>())
                .bind(Bindings.historyLogSearchQueryData().allObjects(), Model.of())
                .bind(Bindings.historyLogSearchQueryData().mainObject(), Model.of())
                .bind(Bindings.historyLogSearchQueryData().object1(), Model.of())
                .bind(Bindings.historyLogSearchQueryData().object2(), Model.of())
                .bind(Bindings.historyLogSearchQueryData().object3(), Model.of())
                .bind(Bindings.historyLogSearchQueryData().object4(), Model.of())
                .bind(
                    Bindings.historyLogSearchQueryData().mandatoryDifferencesEventTypes(),
                    CollectionCopyModel.serializable(Suppliers2.linkedHashSet()))));
  }

  public HistoryLogDataProvider(IModel<HistoryLogSearchQueryData> dataModel) {
    super(dataModel);
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
