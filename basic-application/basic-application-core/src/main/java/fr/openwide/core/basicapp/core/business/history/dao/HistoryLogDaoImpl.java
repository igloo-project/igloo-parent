package fr.openwide.core.basicapp.core.business.history.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;

@Repository
public class HistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<HistoryLog, HistoryEventType>
		implements IHistoryLogDao {

}
