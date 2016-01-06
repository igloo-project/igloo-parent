package fr.openwide.core.jpa.batch.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.util.EntityManagerUtils;

public abstract class AbstractBatchExecutor<T extends AbstractBatchExecutor<T>> {
	
	protected int batchSize;

	@Autowired
	protected IHibernateSearchService hibernateSearchService;
	
	@Autowired
	protected IEntityService entityService;
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;
	
	protected TransactionTemplate readOnlyTransactionTemplate;

	protected TransactionTemplate writeTransactionTemplate;
	
	public T batchSize(int batchSize) {
		this.batchSize = batchSize;
		return thisAsT();
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
	
	protected abstract T thisAsT();

}
