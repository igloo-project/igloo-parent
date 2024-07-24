package org.iglooproject.jpa.more.business.history.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;

public abstract class AbstractHistoryLogDaoImpl<
        HL extends AbstractHistoryLog<HL, HET, ?>, HET extends Enum<HET>>
    extends GenericEntityDaoImpl<Long, HL> implements IGenericHistoryLogDao<HL, HET> {}
