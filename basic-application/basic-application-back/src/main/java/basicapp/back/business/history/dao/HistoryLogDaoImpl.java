package basicapp.back.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import org.springframework.stereotype.Repository;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;

@Repository
public class HistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<HistoryLog, HistoryEventType> implements IHistoryLogDao {

}
