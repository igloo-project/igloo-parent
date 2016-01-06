package fr.openwide.core.jpa.batch.executor;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.commons.util.functional.Joiners;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.QGenericEntity;
import fr.openwide.core.jpa.exception.ServiceException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleHibernateBatchExecutor extends AbstractBatchExecutor<SimpleHibernateBatchExecutor> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHibernateBatchExecutor.class);

	private boolean flushToIndexes;
	
	private List<Class<?>> classesToReindex = Lists.newArrayListWithCapacity(0);
	
	public SimpleHibernateBatchExecutor flushToIndexes(boolean flushToIndexes) {
		this.flushToIndexes = flushToIndexes;
		return this;
	}
	
	public SimpleHibernateBatchExecutor reindexClasses(Class<?> clazz, Class<?>... classes) {
		classesToReindex = Lists.asList(clazz, classes);
		return this;
	}

	public <E extends GenericEntity<Long, ?>> void run(final Class<E> clazz, final List<Long> entityIds,
			final BatchRunnable<E> batchRunnable) {
		LOGGER.info("Beginning batch for class %1$s: %2$d objects", clazz, entityIds.size());
		
		LOGGER.info("    preExecute start");
		
		writeTransactionTemplate.execute(new TransactionCallback<Void>() {
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
					
					batchRunnable.executePartition(entities);
					
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
		
		writeTransactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				batchRunnable.postExecute(entityIds);
				return null;
			}
		});
		
		LOGGER.info("    postExecute end");
		
		if (classesToReindex.size() > 0) {
			LOGGER.info("    reindexing classes %1$s", Joiners.onComma().join(classesToReindex));
			try {
				hibernateSearchService.reindexClasses(classesToReindex);
			} catch (ServiceException e) {
				
			}
			LOGGER.info("    end of reindexing");
		}
		
		LOGGER.info("End of batch for class %1$s: %2$d objects treated", clazz, entityIds.size());
	}

	protected <E extends GenericEntity<Long, ?>> List<E> listEntitiesByIds(Class<E> clazz, Collection<Long> entityIds) {
		PathBuilder<E> path = new PathBuilder<E>(clazz, clazz.getSimpleName());
		QGenericEntity qGenericEntity = new QGenericEntity(path);
		
		return new JPAQuery<E>(entityManagerUtils.getEntityManager()).select(path)
				.from(path)
				.where(qGenericEntity.id.in(entityIds))
				.orderBy(qGenericEntity.id.asc())
				.fetch();
	}

	@Override
	protected SimpleHibernateBatchExecutor thisAsT() {
		return this;
	}
	
}
