package org.iglooproject.jpa.more.business.history.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventType;

public interface IGenericHistoryLogDao<
        HL extends AbstractHistoryLog<HL, HLET, ?>,
        HLET extends Enum<HLET> & IHistoryLogEventType<HLET>>
    extends IGenericEntityDao<Long, HL> {}
