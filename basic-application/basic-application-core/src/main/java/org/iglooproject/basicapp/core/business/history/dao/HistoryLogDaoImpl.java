package org.iglooproject.basicapp.core.business.history.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;

@Repository
public class HistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<HistoryLog, HistoryEventType>
		implements IHistoryLogDao {

}
