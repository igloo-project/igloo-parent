package fr.openwide.core.test.jpa.more.business.history.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryLog;
import fr.openwide.core.test.jpa.more.business.history.model.atomic.TestHistoryEventType;

@Repository
public class TestHistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<TestHistoryLog, TestHistoryEventType>
		implements ITestHistoryLogDao {

}
