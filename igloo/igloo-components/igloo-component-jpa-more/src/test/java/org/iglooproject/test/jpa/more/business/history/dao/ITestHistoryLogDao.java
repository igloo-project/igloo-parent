package org.iglooproject.test.jpa.more.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;
import org.iglooproject.test.jpa.more.business.history.model.TestHistoryLog;
import org.iglooproject.test.jpa.more.business.history.model.atomic.TestHistoryEventType;

public interface ITestHistoryLogDao extends IGenericHistoryLogDao<TestHistoryLog, TestHistoryEventType> {

}
