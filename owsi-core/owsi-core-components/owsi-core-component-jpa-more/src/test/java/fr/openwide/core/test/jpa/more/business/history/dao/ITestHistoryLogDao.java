package fr.openwide.core.test.jpa.more.business.history.dao;

import fr.openwide.core.jpa.more.business.history.dao.IAbstractHistoryLogDao;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryLog;
import fr.openwide.core.test.jpa.more.business.history.model.atomic.TestHistoryEventType;

public interface ITestHistoryLogDao extends IAbstractHistoryLogDao<TestHistoryLog, TestHistoryEventType> {

}
