package fr.openwide.core.jpa.more.business.history.dao;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;

public abstract class AbstractHistoryLogDaoImpl<HL extends AbstractHistoryLog<HL, HET, ?>, HET extends Enum<HET>>
		extends GenericEntityDaoImpl<Long, HL> implements IAbstractHistoryLogDao<HL, HET> {

}
