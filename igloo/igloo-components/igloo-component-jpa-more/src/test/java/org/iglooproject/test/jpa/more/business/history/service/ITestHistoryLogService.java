package org.iglooproject.test.jpa.more.business.history.service;

import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;
import org.iglooproject.test.jpa.more.business.history.model.TestHistoryDifference;
import org.iglooproject.test.jpa.more.business.history.model.TestHistoryLog;
import org.iglooproject.test.jpa.more.business.history.model.atomic.TestHistoryEventType;
import org.iglooproject.test.jpa.more.business.history.model.bean.TestHistoryLogAdditionalInformationBean;

public interface ITestHistoryLogService extends IGenericHistoryLogService<TestHistoryLog, TestHistoryEventType,
		TestHistoryDifference, TestHistoryLogAdditionalInformationBean> {

}
