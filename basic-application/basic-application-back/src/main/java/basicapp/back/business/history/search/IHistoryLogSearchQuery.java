package basicapp.back.business.history.search;

import basicapp.back.business.history.model.HistoryLog;
import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;

public interface IHistoryLogSearchQuery
    extends IJpaSearchQuery<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData> {}
