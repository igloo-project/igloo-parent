package org.iglooproject.jpa.more.business.upgrade.service;

import java.util.Date;
import java.util.List;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.dao.IDataUpgradeRecordDao;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("dataUpgradeRecordService")
public class DataUpgradeRecordServiceImpl extends GenericEntityServiceImpl<Long, DataUpgradeRecord>
    implements IDataUpgradeRecordService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgradeRecordServiceImpl.class);

  private IDataUpgradeRecordDao dataUpgradeRecordDao;

  public DataUpgradeRecordServiceImpl(IDataUpgradeRecordDao dataUpgradeRecordDao) {
    super(dataUpgradeRecordDao);
    this.dataUpgradeRecordDao = dataUpgradeRecordDao;
  }

  @Override
  public DataUpgradeRecord getByDataUpgrade(IDataUpgrade dataUpgrade) {
    return getByNaturalId(dataUpgrade.getName());
  }

  @Override
  public void markAsDone(IDataUpgrade dataUpgrade)
      throws ServiceException, SecurityServiceException {
    markAs(dataUpgrade, true);
  }

  @Override
  public void markAsToDo(IDataUpgrade dataUpgrade)
      throws ServiceException, SecurityServiceException {
    markAs(dataUpgrade, false);
  }

  private void markAs(IDataUpgrade dataUpgrade, boolean done)
      throws ServiceException, SecurityServiceException {
    DataUpgradeRecord record = getByDataUpgrade(dataUpgrade);
    if (record != null) {
      record.setExecutionDate(done ? new Date() : null);
      record.setDone(done);
      update(record);
    } else {
      DataUpgradeRecord newRecord = new DataUpgradeRecord();
      newRecord.setName(dataUpgrade.getName());
      newRecord.setExecutionDate(done ? new Date() : null);
      newRecord.setDone(done);
      create(newRecord);
    }
  }

  @Override
  public boolean isDone(IDataUpgrade dataUpgrade) {
    return dataUpgradeRecordDao.isDone(dataUpgrade);
  }

  @Override
  public List<DataUpgradeRecord> listAutoPerform() {
    return dataUpgradeRecordDao.listAutoPerform();
  }

  @Override
  public void disableAutoPerform(IDataUpgrade dataUpgrade)
      throws ServiceException, SecurityServiceException {
    DataUpgradeRecord record = getByDataUpgrade(dataUpgrade);
    if (record != null) {
      record.setAutoPerform(false);
      update(record);
    } else {
      LOGGER.warn(
          "Auto perform cannot be disabled because no record has been found for '"
              + dataUpgrade.getName()
              + "'");
    }
  }
}
