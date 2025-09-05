package test.business.dao;

import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;
import test.business.model.TestHistoryEventType;
import test.business.model.TestHistoryLog;

public interface ITestHistoryLogDao
    extends IGenericHistoryLogDao<TestHistoryLog, TestHistoryEventType> {}
