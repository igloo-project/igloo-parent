package test.jpa.more.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import org.springframework.stereotype.Repository;
import test.jpa.more.business.history.model.TestHistoryLog;
import test.jpa.more.business.history.model.atomic.TestHistoryLogEventType;

@Repository
public class TestHistoryLogDaoImpl
    extends AbstractHistoryLogDaoImpl<TestHistoryLog, TestHistoryLogEventType>
    implements ITestHistoryLogDao {}
