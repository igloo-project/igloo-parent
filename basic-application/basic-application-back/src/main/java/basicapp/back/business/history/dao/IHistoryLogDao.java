package basicapp.back.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;

public interface IHistoryLogDao extends IGenericHistoryLogDao<HistoryLog, HistoryEventType> {

}
