package basicapp.back.business.history.search;

import basicapp.back.business.history.model.HistoryLog;
import org.iglooproject.jpa.more.search.query.ISearchQuery;

public interface IHistoryLogSearchQuery
    extends ISearchQuery<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData> {}
