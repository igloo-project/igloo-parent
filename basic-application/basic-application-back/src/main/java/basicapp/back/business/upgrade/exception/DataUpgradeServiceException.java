package basicapp.back.business.upgrade.exception;

import org.iglooproject.jpa.exception.ServiceException;

public class DataUpgradeServiceException extends ServiceException {

  private static final long serialVersionUID = 1L;

  public DataUpgradeServiceException(String message, Throwable e) {
    super(message, e);
  }

  public DataUpgradeServiceException(Throwable e) {
    super(e);
  }
}
