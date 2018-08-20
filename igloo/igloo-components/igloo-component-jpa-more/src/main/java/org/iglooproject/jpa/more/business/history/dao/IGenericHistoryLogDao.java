package org.iglooproject.jpa.more.business.history.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;

public interface IGenericHistoryLogDao<HL extends AbstractHistoryLog<HL, HET, ?>, HET extends Enum<HET>>
		extends IGenericEntityDao<Long, HL> {

}
