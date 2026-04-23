package basicapp.back.business.history.dao;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryLogEventType;
import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;

public interface IHistoryLogDao extends IGenericHistoryLogDao<HistoryLog, HistoryLogEventType> {}
