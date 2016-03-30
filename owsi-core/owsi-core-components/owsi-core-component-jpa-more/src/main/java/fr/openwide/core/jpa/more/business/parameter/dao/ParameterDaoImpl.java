package fr.openwide.core.jpa.more.business.parameter.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;

@SuppressWarnings("deprecation")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Long, Parameter> implements IParameterDao, IMutablePropertyDao {
	
	private TransactionTemplate readOnlyTransactionTemplate;

	private TransactionTemplate writeTransactionTemplate;
	
	@Override
	public Parameter getByName(String name) {
		return super.getByNaturalId(name);
	}
	
	@Override
	public String getInTransaction(final String key) {
		return readOnlyTransactionTemplate.execute(new TransactionCallback<String>() {
			@Override
			public String doInTransaction(TransactionStatus status) {
				return get(key);
			}
		});
	}

	@Override
	public void setInTransaction(final String key, final String value) throws ServiceException, SecurityServiceException {
		writeTransactionTemplate.execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						try {
							set(key, value);
						} catch (Exception e) {
							throw new IllegalStateException(String.format("Error while updating property '%1s'.", key), e);
						}
					}
				}
		);
	}

	@Override
	public void cleanInTransaction() {
		writeTransactionTemplate.execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						clean();
					}
				}
		);
	}
	
	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute readOnlyTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		readOnlyTransactionAttribute.setReadOnly(true);
		readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);
		
		DefaultTransactionAttribute writeTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
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
