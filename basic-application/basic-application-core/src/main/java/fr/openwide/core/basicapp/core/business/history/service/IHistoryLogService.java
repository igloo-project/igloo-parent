package fr.openwide.core.basicapp.core.business.history.service;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.basicapp.core.business.history.model.bean.HistoryLogAdditionalInformationBean;
import fr.openwide.core.jpa.more.business.history.service.IGenericHistoryLogService;

public interface IHistoryLogService extends IGenericHistoryLogService<HistoryLog, HistoryEventType, HistoryDifference, HistoryLogAdditionalInformationBean> {

}
