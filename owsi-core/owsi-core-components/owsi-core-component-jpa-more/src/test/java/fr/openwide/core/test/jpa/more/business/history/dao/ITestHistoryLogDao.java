package fr.openwide.core.test.jpa.more.business.history.dao;

import fr.openwide.core.jpa.more.business.history.dao.IGenericHistoryLogDao;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryLog;
import fr.openwide.core.test.jpa.more.business.history.model.atomic.TestHistoryEventType;

public interface ITestHistoryLogDao extends IGenericHistoryLogDao<TestHistoryLog, TestHistoryEventType> {

}
