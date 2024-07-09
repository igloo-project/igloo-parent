package basicapp.back.business.history.search;

import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

import basicapp.back.business.history.model.HistoryLog;

public interface IHistoryLogSearchQuery extends IHibernateSearchSearchQuery<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData> {

}
