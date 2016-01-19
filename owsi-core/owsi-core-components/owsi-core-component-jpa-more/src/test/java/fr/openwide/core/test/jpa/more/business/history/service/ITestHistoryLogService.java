package fr.openwide.core.test.jpa.more.business.history.service;

import fr.openwide.core.jpa.more.business.history.service.IGenericHistoryLogService;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryDifference;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryLog;
import fr.openwide.core.test.jpa.more.business.history.model.atomic.TestHistoryEventType;
import fr.openwide.core.test.jpa.more.business.history.model.bean.TestHistoryLogAdditionalInformationBean;

public interface ITestHistoryLogService extends IGenericHistoryLogService<TestHistoryLog, TestHistoryEventType,
		TestHistoryDifference, TestHistoryLogAdditionalInformationBean> {

}
