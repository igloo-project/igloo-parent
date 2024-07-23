package org.iglooproject.jpa.more.business.upgrade.service;

import java.util.List;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;

public interface IDataUpgradeRecordService extends IGenericEntityService<Long, DataUpgradeRecord> {

  DataUpgradeRecord getByDataUpgrade(IDataUpgrade dataUpgrade);

  void markAsDone(IDataUpgrade dataUpgrade) throws ServiceException, SecurityServiceException;

  void markAsToDo(IDataUpgrade dataUpgrade) throws ServiceException, SecurityServiceException;

  boolean isDone(IDataUpgrade dataUpgrade);

  List<DataUpgradeRecord> listAutoPerform();

  void disableAutoPerform(IDataUpgrade dataUpgrade)
      throws ServiceException, SecurityServiceException;
}
