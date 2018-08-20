package org.iglooproject.test.jpa.more.business.history.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import org.iglooproject.test.jpa.more.business.history.model.TestHistoryLog;
import org.iglooproject.test.jpa.more.business.history.model.atomic.TestHistoryEventType;

@Repository
public class TestHistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<TestHistoryLog, TestHistoryEventType>
		implements ITestHistoryLogDao {

}
