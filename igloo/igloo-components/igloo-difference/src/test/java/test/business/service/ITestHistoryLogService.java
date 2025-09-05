package test.business.service;

import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;
import test.business.model.HistoryLogAdditionalInformationBean;
import test.business.model.TestHistoryDifference;
import test.business.model.TestHistoryEventType;
import test.business.model.TestHistoryLog;

public interface ITestHistoryLogService
    extends IGenericHistoryLogService<
        TestHistoryLog,
        TestHistoryEventType,
        TestHistoryDifference,
        HistoryLogAdditionalInformationBean> {}
