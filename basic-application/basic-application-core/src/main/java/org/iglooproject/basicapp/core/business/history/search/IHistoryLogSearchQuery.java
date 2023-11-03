package org.iglooproject.basicapp.core.business.history.search;

import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IHistoryLogSearchQuery extends IHibernateSearchSearchQuery<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData> {

}
