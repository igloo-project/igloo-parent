package basicapp.back.business.upgrade.model;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
public class DataUpgrade_InitStorageUnit implements IDataUpgrade {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgrade_InitStorageUnit.class);

  @Override
  public String getName() {
    return DataUpgrade_InitStorageUnit.class.getSimpleName();
  }

  @Override
  public void perform() throws ServiceException, SecurityServiceException {
    LOGGER.info("Performing StorageUnit init upgrade");

    // Complete here
  }
}
