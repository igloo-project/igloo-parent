package basicapp.back.business.history.dao;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryLogEventType;
import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class HistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<HistoryLog, HistoryLogEventType>
    implements IHistoryLogDao {}
