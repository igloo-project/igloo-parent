package test.jpa.more.business.history.service;

import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;
import test.jpa.more.business.history.model.TestHistoryDifference;
import test.jpa.more.business.history.model.TestHistoryLog;
import test.jpa.more.business.history.model.atomic.TestHistoryEventType;
import test.jpa.more.business.history.model.bean.TestHistoryLogAdditionalInformationBean;

public interface ITestHistoryLogService
    extends IGenericHistoryLogService<
        TestHistoryLog,
        TestHistoryEventType,
        TestHistoryDifference,
        TestHistoryLogAdditionalInformationBean> {}
