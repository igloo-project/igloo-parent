package test.business.dao;

import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;
import test.business.model.TestHistoryLog;
import test.business.model.TestHistoryLogEventType;

public interface ITestHistoryLogDao
    extends IGenericHistoryLogDao<TestHistoryLog, TestHistoryLogEventType> {}
