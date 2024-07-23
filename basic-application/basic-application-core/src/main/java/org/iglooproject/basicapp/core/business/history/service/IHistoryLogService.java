package org.iglooproject.basicapp.core.business.history.service;

import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.history.model.bean.HistoryLogAdditionalInformationBean;
import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;

public interface IHistoryLogService
    extends IGenericHistoryLogService<
        HistoryLog, HistoryEventType, HistoryDifference, HistoryLogAdditionalInformationBean> {}
