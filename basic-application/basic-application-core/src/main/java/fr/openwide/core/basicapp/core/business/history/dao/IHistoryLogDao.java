package fr.openwide.core.basicapp.core.business.history.dao;

import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.jpa.more.business.history.dao.IGenericHistoryLogDao;

public interface IHistoryLogDao extends IGenericHistoryLogDao<HistoryLog, HistoryEventType> {

}
