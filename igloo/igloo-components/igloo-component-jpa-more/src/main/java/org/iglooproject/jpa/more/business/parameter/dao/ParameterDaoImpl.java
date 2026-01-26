package org.iglooproject.jpa.more.business.parameter.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.parameter.model.Parameter;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

public class ParameterDaoImpl extends GenericEntityDaoImpl<Long, Parameter>
    implements IMutablePropertyDao {

  private TransactionTemplate readOnlyTransactionTemplate;

  private TransactionTemplate writeTransactionTemplate;

  private Parameter getByName(String name) {
    return super.getByNaturalId(name);
  }

  @Override
  public String getInTransaction(final String key) {
    return readOnlyTransactionTemplate.execute(status -> get(key));
  }

  @Override
  public void setInTransaction(final String key, final String value)
      throws ServiceException, SecurityServiceException {
    writeTransactionTemplate.executeWithoutResult(
        status -> {
          try {
            set(key, value);
          } catch (RuntimeException | ServiceException | SecurityServiceException e) {
            throw new IllegalStateException(
                String.format("Error while updating property '%1s'.", key), e);
          }
        });
  }

  @Override
  public void cleanInTransaction() {
    writeTransactionTemplate.executeWithoutResult(status -> clean());
  }

  @Autowired
  public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
    DefaultTransactionAttribute readOnlyTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
    readOnlyTransactionAttribute.setReadOnly(true);
    readOnlyTransactionTemplate =
        new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);

    DefaultTransactionAttribute writeTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
    writeTransactionAttribute.setReadOnly(false);
    writeTransactionTemplate =
        new TransactionTemplate(transactionManager, writeTransactionAttribute);
  }

  private String get(String key) {
    Parameter parameter = getByName(key);
    if (parameter == null) {
      return null;
    }
    return parameter.getStringValue();
  }

  private void set(String key, String value) throws ServiceException, SecurityServiceException {
    Parameter parameter = getByName(key);
    if (parameter != null) {
      parameter.setStringValue(value);
      update(parameter);
    } else {
      save(new Parameter(key, value));
    }
  }

  private void clean() {
    for (Parameter parameter : list()) {
      delete(parameter);
    }
  }
}
