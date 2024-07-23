package test.jpa.more.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;
import test.jpa.more.business.history.model.TestHistoryLog;
import test.jpa.more.business.history.model.atomic.TestHistoryEventType;

public interface ITestHistoryLogDao
    extends IGenericHistoryLogDao<TestHistoryLog, TestHistoryEventType> {}
