package test.business.dao;

import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import test.business.model.TestHistoryLog;
import test.business.model.TestHistoryLogEventType;

public class TestHistoryLogDaoImpl
    extends AbstractHistoryLogDaoImpl<TestHistoryLog, TestHistoryLogEventType>
    implements ITestHistoryLogDao {}
