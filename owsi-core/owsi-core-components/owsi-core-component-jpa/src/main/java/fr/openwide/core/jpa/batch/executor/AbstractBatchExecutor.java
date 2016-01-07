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
	
	protected TransactionTemplate readOnlyRequiredTransactionTemplate;

	protected TransactionTemplate writeRequiredTransactionTemplate;
	
	protected TransactionTemplate readOnlyRequiresNewTransactionTemplate;

	protected TransactionTemplate writeRequiresNewTransactionTemplate;
	
	public T batchSize(int batchSize) {
		this.batchSize = batchSize;
		return thisAsT();
	}
	
	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute readOnlyRequiresNewTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		readOnlyRequiresNewTransactionAttribute.setReadOnly(true);
		readOnlyRequiresNewTransactionTemplate =
				new TransactionTemplate(transactionManager, readOnlyRequiresNewTransactionAttribute);
		
		DefaultTransactionAttribute writeRequiresNewTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		writeRequiresNewTransactionAttribute.setReadOnly(false);
		writeRequiresNewTransactionTemplate =
				new TransactionTemplate(transactionManager, writeRequiresNewTransactionAttribute);
		
		DefaultTransactionAttribute readOnlyRequiredTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		readOnlyRequiredTransactionAttribute.setReadOnly(true);
		readOnlyRequiredTransactionTemplate =
				new TransactionTemplate(transactionManager, readOnlyRequiredTransactionAttribute);
		
		DefaultTransactionAttribute writeRequiredTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeRequiredTransactionAttribute.setReadOnly(false);
		writeRequiredTransactionTemplate =
				new TransactionTemplate(transactionManager, writeRequiredTransactionAttribute);
	}
	
	protected abstract T thisAsT();

}
