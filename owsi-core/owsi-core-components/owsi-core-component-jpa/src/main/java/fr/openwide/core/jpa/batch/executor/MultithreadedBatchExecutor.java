package fr.openwide.core.jpa.batch.executor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.batch.processor.ThreadedProcessor;
import fr.openwide.core.jpa.batch.util.ProcessorProgressLogger;
import fr.openwide.core.spring.property.service.IPropertyService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MultithreadedBatchExecutor extends AbstractBatchExecutor<MultithreadedBatchExecutor> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MultithreadedBatchExecutor.class);
	
	private static final Logger PROGRESS_LOGGER = LoggerFactory.getLogger(ProcessorProgressLogger.class);
	
	@Autowired
	private IPropertyService propertyService;
	
	private int threads = 2;
	
	private int timeoutInMinutes = 15;
	
	private boolean abortAllOnExecutionError = true;
	
	public MultithreadedBatchExecutor threads(int threads) {
		this.threads = threads;
		return this;
	}
	
	public MultithreadedBatchExecutor timeoutInMinutes(int timeout) {
		this.timeoutInMinutes = timeout;
		return this;
	}
	
	public MultithreadedBatchExecutor abortAllOnExecutionError(boolean abortAllOnExecutionError) {
		this.abortAllOnExecutionError = abortAllOnExecutionError;
		return this;
	}
	
	public void run(String context, final List<Long> entityIds, final IBatchRunnable<Long> batchRunnable) {
		Date startTime = new Date();
		
		LOGGER.info("Beginning batch for %1$s: %2$d objects", context, entityIds.size());

		try {
			LOGGER.info("    preExecute start");
		
			writeRequiresNewTransactionTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					batchRunnable.preExecute();
					return null;
				}
			});
			
			LOGGER.info("    preExecute end");
	
			LOGGER.info("    starting batch executions");
			
			List<List<Long>> entityIdsPartitions = Lists.partition(entityIds, batchSize);
			List<Runnable> runnables = Lists.newArrayList();
			for (final List<Long> entityPartition : entityIdsPartitions) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						batchRunnable.executePartition(entityPartition);
					}
				};
				runnables.add(runnable);
			}
			
			createThreadedProcessor(batchSize, timeoutInMinutes)
					.runWithTransaction(context, runnables, writeRequiresNewTransactionTemplate, entityIds.size());
			
			LOGGER.info("    end of batch executions");
	
			LOGGER.info("    postExecute start");
			
			writeRequiresNewTransactionTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					batchRunnable.postExecute();
					return null;
				}
			});
			
			LOGGER.info("    postExecute end");
			
			logEnd(context, startTime, null);
		} catch (ExecutionException e) {
			logEnd(context, startTime, e);
			try {
				LOGGER.info("    onError start");
				batchRunnable.onError(e);
				LOGGER.info("    onError end (exception was NOT re-thrown)");
			} finally {
				LOGGER.info("    onError end (exception WAS re-thrown)");
			}
		}
	}

	protected final ThreadedProcessor createThreadedProcessor(int maxLoggingIncrement, int timeoutInMinutes) {
		return new ThreadedProcessor(
				threads,
				timeoutInMinutes, TimeUnit.MINUTES,
				1, TimeUnit.MINUTES,
				abortAllOnExecutionError,
				2, TimeUnit.SECONDS,  // intervalle minimum pour le logging
				30, TimeUnit.SECONDS, // intervalle de temps maxi entre deux logs
				maxLoggingIncrement,  // intervalle de nombre d'éléments traités maxi entre deux logs
				PROGRESS_LOGGER
		);
	}
	
	protected void logEnd(String context, Date startTime, Exception e) {
		long duration = new Date().getTime() - startTime.getTime();
		
		StringBuilder sb = new StringBuilder(String.format("%1$s - Migrated items ", context));
		if (duration < 1000) {
			sb.append(String.format("in %1$s ms", duration));
		} else if (duration < 60000){
			sb.append(String.format("in %1$s s", new BigDecimal(duration / 1000f).setScale(3, BigDecimal.ROUND_HALF_UP).toString()));
		} else {
			sb.append(String.format("in %1$s mn", new BigDecimal(duration / 60000f).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
		}
		if (e != null) {
			// Log this as info anyway, since error handling is done elsewhere
			sb.append(String.format(", but caught exception '%s'.", e));
		}
		PROGRESS_LOGGER.info(sb.toString());
	}

	@Override
	protected MultithreadedBatchExecutor thisAsT() {
		return this;
	}

}
