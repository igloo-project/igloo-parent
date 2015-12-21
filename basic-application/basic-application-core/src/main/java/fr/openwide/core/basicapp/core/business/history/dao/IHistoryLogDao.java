package fr.openwide.core.basicapp.core.business.history.dao;

import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.jpa.more.business.history.dao.IAbstractHistoryLogDao;

public interface IHistoryLogDao extends IAbstractHistoryLogDao<HistoryLog, HistoryEventType> {

}
