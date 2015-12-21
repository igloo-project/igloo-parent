package fr.openwide.core.basicapp.core.business.history.service;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.jpa.more.business.history.service.IAbstractHistoryLogService;

public interface IHistoryLogService extends IAbstractHistoryLogService<HistoryLog, HistoryEventType, HistoryDifference> {

}
