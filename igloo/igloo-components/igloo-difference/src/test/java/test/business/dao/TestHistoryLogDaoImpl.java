package test.business.dao;

import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import test.business.model.TestHistoryEventType;
import test.business.model.TestHistoryLog;

public class TestHistoryLogDaoImpl
    extends AbstractHistoryLogDaoImpl<TestHistoryLog, TestHistoryEventType>
    implements ITestHistoryLogDao {}
