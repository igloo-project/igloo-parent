package fr.openwide.core.jpa.batch.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.batch.runnable.Writeability;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.util.EntityManagerUtils;

public abstract class AbstractBatchExecutor<T extends AbstractBatchExecutor<T>> {
	
	protected int batchSize;
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	protected IHibernateSearchService hibernateSearchService;
	
	@Autowired
	protected IEntityService entityService;
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;
	
	public T batchSize(int batchSize) {
		this.batchSize = batchSize;
		return thisAsT();
	}
	
	public TransactionTemplate newTransactionTemplate(Writeability writeability, int propagation) {
		DefaultTransactionAttribute transactionAttribute =
				new DefaultTransactionAttribute(propagation);
		transactionAttribute.setReadOnly(Writeability.READ_ONLY.equals(writeability));
		return new TransactionTemplate(transactionManager, transactionAttribute);
	}
	
	protected abstract T thisAsT();

}
