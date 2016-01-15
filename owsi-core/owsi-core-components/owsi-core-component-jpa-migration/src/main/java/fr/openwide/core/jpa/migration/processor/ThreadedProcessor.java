package fr.openwide.core.jpa.migration.processor;

import static fr.openwide.core.jpa.more.property.JpaMorePropertyIds.MIGRATION_LOGGING_MEMORY;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.migration.monitor.ProcessorMonitorContext;
import fr.openwide.core.jpa.migration.monitor.ThreadLocalInitializingCallable;
import fr.openwide.core.jpa.migration.transaction.TransactionWrapperCallable;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.spring.util.StringUtils;

public class ThreadedProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedProcessor.class);

	private final int threadPoolSize;
	private final int keepAliveTime;
	private final TimeUnit keepAliveTimeUnit;
	private final int maxTotalDuration;
	private final TimeUnit maxTotalDurationTimeUnit;
	private final Integer loggingCheckIntervalTime;
	private final TimeUnit loggingCheckIntervalTimeUnit;
	private final Integer maxLoggingTime;
	private final TimeUnit maxLoggingTimeUnit;
	private final Integer maxLoggingIncrement;
	private final Logger progressLogger;
	private final IPropertyService propertyService;
	
	private String loggerContext;

	private ProcessorMonitorContext monitorContext;

	public ThreadedProcessor(int threadPoolSize,
			int maxTotalDuration, TimeUnit maxTotalDurationUnit,
			int keepAliveTime, TimeUnit keepAliveTimeUnit, IPropertyService propertyService) {
		this(threadPoolSize, maxTotalDuration, maxTotalDurationUnit, keepAliveTime, keepAliveTimeUnit, propertyService,
				null, null, null, null, null, null);
	}

	public ThreadedProcessor(int threadPoolSize,
			int maxTotalDuration, TimeUnit maxTotalDurationUnit,
			int keepAliveTime, TimeUnit keepAliveTimeUnit,
			IPropertyService propertyService,
			Integer loggingCheckIntervalTime, TimeUnit loggingCheckIntervalTimeUnit,
			Integer maxLoggingTime, TimeUnit maxLoggingTimeUnit,
			Integer maxLoggingIncrement,
			Logger progressLogger) {
		super();
		this.threadPoolSize = threadPoolSize;
		this.keepAliveTime = keepAliveTime;
		this.keepAliveTimeUnit = keepAliveTimeUnit;
		this.maxTotalDuration = maxTotalDuration;
		this.maxTotalDurationTimeUnit = maxTotalDurationUnit;
		this.loggingCheckIntervalTime = loggingCheckIntervalTime;
		this.loggingCheckIntervalTimeUnit = loggingCheckIntervalTimeUnit;
		this.maxLoggingTime = maxLoggingTime;
		this.maxLoggingTimeUnit = maxLoggingTimeUnit;
		this.maxLoggingIncrement = maxLoggingIncrement;
		this.progressLogger = progressLogger;
		this.propertyService = propertyService;
	}

	public <T> List<Future<T>> runWithoutTransaction(String loggerContext, List<Callable<T>> callables) {
		return runWithoutTransaction(loggerContext, callables, null);
	}

	public <T> List<Future<T>> runWithoutTransaction(String loggerContext, List<Callable<T>> callables, Integer totalItems) {
		return runWithTransaction(loggerContext, callables, null, totalItems);
	}

	public <T> List<Future<T>> runWithTransaction(String loggerContext, List<Callable<T>> callables, TransactionTemplate transactionTemplate, Integer totalItems) {
		this.loggerContext = loggerContext;
		
		List<Future<T>> futures = Lists.newArrayList();
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, keepAliveTime, keepAliveTimeUnit, workQueue);
		executor.prestartAllCoreThreads();
		
		Thread loggingThread = null;
		this.monitorContext = new ProcessorMonitorContext();
		if (progressLogger != null) {
			loggingThread = new Thread(new LoggingRunnable());
		}
		if (totalItems != null) {
			this.monitorContext.getTotalItems().set(totalItems);
		}
		try {
			// on wrappe toutes les exécutions dans des transactions
			for (Callable<T> callable : callables) {
				Callable<T> wrappedCallable = new ThreadLocalInitializingCallable<>(callable, ProcessorMonitorContext.getThreadLocal(), monitorContext);
				if (transactionTemplate != null) {
					futures.add(executor.submit(new TransactionWrapperCallable<T>(transactionTemplate, wrappedCallable)));
				} else {
					futures.add(executor.submit(wrappedCallable));
				}
			}
			
			executor.shutdown();
			if (loggingThread != null) {
				loggingThread.start();
			}
			
			List<Future<T>> terminatedFutures = Lists.newArrayList();
			boolean interrupted = false;
			
			try {
				boolean terminated = executor.awaitTermination(maxTotalDuration, maxTotalDurationTimeUnit);
				if (!terminated) {
					LOGGER.error("Les tâches n'ont pas été terminées avant expiration du timeout de {} {}", maxTotalDuration, maxTotalDurationTimeUnit.name());
				}
				LOGGER.info("{} - {} éléments importés", loggerContext, this.monitorContext.getDoneItems());
				if (this.monitorContext.getFailedItems().get() > 0) {
					LOGGER.error("{} - {} élément(s) en erreur", loggerContext, this.monitorContext.getFailedItems().get());
				}
				if (this.monitorContext.getIgnoredItems().get() > 0) {
					LOGGER.info("{} - {} élément(s) ignoré(s)", loggerContext, this.monitorContext.getIgnoredItems().get());
				}
				
				for (Future<T> future : futures) {
					try {
						future.get(0, TimeUnit.SECONDS);
						if (future.isDone()) {
							terminatedFutures.add(future);
						} else {
							interrupted = true;
						}
					} catch (ExecutionException e) {
						// Ne peut pas arriver car tous les callables sont wrappés dans un callable qui catche l'exception
						throw new IllegalStateException("Une tâche d'import d'objets a échoué ; interruption de l'import.", e);
					} catch (TimeoutException timeoutException) {
						// déjà notifié par un warn
						future.cancel(true);
						interrupted = true;
					}
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException("Erreur d'import : timeout.", e);
			}
			
			if (interrupted) {
				throw new IllegalStateException("Erreur d'import : interruption d'un thread de traitement.");
			}
			
			return terminatedFutures;
		} finally {
			ProcessorMonitorContext.unset();
			if (loggingThread != null) {
				loggingThread.interrupt();
				try {
					loggingThread.join();
				} catch (InterruptedException e) {
					LOGGER.warn("Thread interrompu pendant l'attente de la fin de l'exécution du thread de logging d'avancement.");
				}
			}
		}
	}

	protected class LoggingRunnable implements Runnable {
		
		private long startTime;
		
		private long lastLoggingTime;
		
		private int lastDoneItems;
		
		@Override
		public void run() {
			startTime = System.currentTimeMillis();
			lastLoggingTime = startTime;
			lastDoneItems = 0;
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(loggingCheckIntervalTimeUnit.toMillis(loggingCheckIntervalTime));
					log(false);
				} catch (InterruptedException e) {
					log(true);
					LOGGER.info("Thread de logging d'avancement interrompu.");
					return;
				}
			}
			log(true);
		}
		
		public void log(boolean force) {
			long currentTime = System.currentTimeMillis();
			int totalItems = monitorContext.getTotalItems().get();
			int doneItems = monitorContext.getDoneItems().get();
			int ignoredItems = monitorContext.getIgnoredItems().get();
			if (force
					|| (currentTime - lastLoggingTime) > maxLoggingTimeUnit.toMillis(maxLoggingTime)
					|| (doneItems - lastDoneItems) > maxLoggingIncrement) {
				Float speedSinceStart = (float) doneItems / (float) (currentTime - startTime);
				int roundedSpeedSinceStart = Math.round(speedSinceStart * 1000);
				
				Float speedSinceLast = (float) (doneItems - lastDoneItems) / (float) (currentTime - lastLoggingTime);
				int roundedSpeedSinceLast = Math.round(speedSinceLast * 1000);
				
				lastLoggingTime = currentTime;
				lastDoneItems = doneItems;
				
				StringBuilder sb = new StringBuilder();
				if (StringUtils.hasText(loggerContext)) {
					sb.append(loggerContext).append(" - ");
				}
				sb.append("Avancement {} / {} ({} ignorés, {} items / s. depuis début, {} items / s. depuis dernier log)");
				
				if (propertyService.get(MIGRATION_LOGGING_MEMORY)) {
					sb.append(" - Mémoire disponible {} / {}");
					progressLogger.info(sb.toString(), doneItems, totalItems - ignoredItems, ignoredItems,
							roundedSpeedSinceStart, roundedSpeedSinceLast,
							StringUtils.humanReadableByteCount(Runtime.getRuntime().freeMemory(), true),
							StringUtils.humanReadableByteCount(Runtime.getRuntime().totalMemory(), true));
				} else {
					progressLogger.info(sb.toString(), doneItems, totalItems - ignoredItems, ignoredItems,
							roundedSpeedSinceStart, roundedSpeedSinceLast);
				}
			}
		}
	}

}
