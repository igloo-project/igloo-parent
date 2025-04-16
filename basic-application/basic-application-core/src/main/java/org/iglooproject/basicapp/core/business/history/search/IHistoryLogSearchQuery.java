package org.iglooproject.basicapp.core.business.history.search;

import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.jpa.more.business.search.query.IJpaSearchQuery;

public interface IHistoryLogSearchQuery
    extends IJpaSearchQuery<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData> {}
