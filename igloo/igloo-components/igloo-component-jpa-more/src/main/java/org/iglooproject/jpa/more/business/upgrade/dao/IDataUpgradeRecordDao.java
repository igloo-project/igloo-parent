package org.iglooproject.jpa.more.business.upgrade.dao;

import java.util.List;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;

public interface IDataUpgradeRecordDao extends IGenericEntityDao<Long, DataUpgradeRecord> {

  boolean isDone(IDataUpgrade dataUpgrade);

  List<DataUpgradeRecord> listAutoPerform();
}
