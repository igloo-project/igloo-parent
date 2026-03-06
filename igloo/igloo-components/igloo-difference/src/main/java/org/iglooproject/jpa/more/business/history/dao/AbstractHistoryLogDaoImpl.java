package org.iglooproject.jpa.more.business.history.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventType;

public abstract class AbstractHistoryLogDaoImpl<
        HL extends AbstractHistoryLog<HL, HLET, ?>,
        HLET extends Enum<HLET> & IHistoryLogEventType<HLET>>
    extends GenericEntityDaoImpl<Long, HL> implements IGenericHistoryLogDao<HL, HLET> {}
