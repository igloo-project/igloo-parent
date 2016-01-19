package fr.openwide.core.jpa.more.business.history.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;

public interface IGenericHistoryLogDao<HL extends AbstractHistoryLog<HL, HET, ?>, HET extends Enum<HET>>
		extends IGenericEntityDao<Long, HL> {

}
