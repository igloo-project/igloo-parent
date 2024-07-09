package basicapp.back.business.history.service;

import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;

import basicapp.back.business.history.model.HistoryDifference;
import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;

public interface IHistoryLogService extends IGenericHistoryLogService<HistoryLog, HistoryEventType,
		HistoryDifference, HistoryLogAdditionalInformationBean> {

}
