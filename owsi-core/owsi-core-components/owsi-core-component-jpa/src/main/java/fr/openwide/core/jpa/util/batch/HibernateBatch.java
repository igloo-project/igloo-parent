package fr.openwide.core.jpa.util.batch;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.QGenericEntity;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.util.EntityManagerUtils;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HibernateBatch {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateBatch.class);

	private int batchSize;

	private boolean flushToIndexes;
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	private TransactionTemplate readOnlyTransactionTemplate;

	private TransactionTemplate writeTransactionTemplate;

	public HibernateBatch batchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	public HibernateBatch flushToIndexes(boolean flushToIndexes) {
		this.flushToIndexes = flushToIndexes;
		return this;
	}

	public <E extends GenericEntity<Long, ?>> void run(final Class<E> clazz, final List<Long> entityIds,
			final BatchRunnable<E> batchRunnable) {
		LOGGER.info("Beginning batch for class %1$s: %2$d objects", clazz, entityIds.size());
		
		LOGGER.info("    preExecute start");
		
		readOnlyTransactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				batchRunnable.preExecute(entityIds);
				return null;
			}
		});
		
		LOGGER.info("    preExecute end");

		LOGGER.info("    starting batch executions");
		
		List<List<Long>> entityIdsPartitions = Lists.partition(entityIds, batchSize);
		int i = 0;
		for (final List<Long> entityIdsPartition : entityIdsPartitions) {
			writeTransactionTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					List<E> entities = listEntitiesByIds(clazz, entityIdsPartition);
					
					batchRunnable.preExecutePartition(entities);
					
					for (E entity : entities) {
						batchRunnable.run(entity);
					}
					
					batchRunnable.postExecutePartition(entities);
					
					entityService.flush();
					if (flushToIndexes) {
						hibernateSearchService.flushToIndexes();
					}
					entityService.clear();
					return null;
				}
			});
			
			i += entityIdsPartition.size();
			
			LOGGER.info("        treated %1$d/%2$d objects", i, entityIds.size());
		}
		
		LOGGER.info("    end of batch executions");

		LOGGER.info("    postExecute start");
		
		readOnlyTransactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				batchRunnable.postExecute(entityIds);
				return null;
			}
		});
		
		LOGGER.info("    postExecute end");
		
		LOGGER.info("End of batch for class %1$s: %2$d objects treated", clazz, entityIds.size());
	}
	
	public final <E extends GenericEntity<Long, ?>> List<E> listEntitiesByIds(Class<E> clazz, Collection<Long> entityIds) {
		PathBuilder<E> path = new PathBuilder<E>(clazz, clazz.getSimpleName());
		QGenericEntity qGenericEntity = new QGenericEntity(path);
		
		return new JPAQuery<E>(entityManagerUtils.getEntityManager()).select(path)
				.from(path)
				.where(qGenericEntity.id.in(entityIds))
				.orderBy(qGenericEntity.id.asc())
				.fetch();
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

}
