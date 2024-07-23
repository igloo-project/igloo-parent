package org.iglooproject.basicapp.core.business.history.dao;

import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;

public interface IHistoryLogDao extends IGenericHistoryLogDao<HistoryLog, HistoryEventType> {}
