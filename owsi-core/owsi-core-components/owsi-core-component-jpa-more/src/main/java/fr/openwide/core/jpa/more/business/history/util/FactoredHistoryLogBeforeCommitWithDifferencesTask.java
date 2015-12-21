package fr.openwide.core.jpa.more.business.history.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.service.ITransactionScopeIndependantRunnerService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;

public class FactoredHistoryLogBeforeCommitWithDifferencesTask implements ITransactionSynchronizationBeforeCommitTask {
	
	@Autowired
	private ITransactionScopeIndependantRunnerService transactionScopeIndependantRunnerService;
	
	private final Set<HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?>> tasks;

	public FactoredHistoryLogBeforeCommitWithDifferencesTask(Set<HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?>> tasks) {
		super();
		this.tasks = tasks;
	}

	@Override
	public void run() throws Exception {
		final List<HistoryLogRunner<?>> runners = Lists.newArrayList();
		for (HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?> task : tasks) {
			runners.add(new HistoryLogRunner<>(task));
		}

		for (HistoryLogRunner<?> runner : runners) {
			runner.prepareRetrieval();
		}
		
		/*
		 * Ici on met en oeuvre le seul intérêt de cette classe : exécuter toutes les récupérations d'objets depuis la
		 * BDD en une seule transaction, même s'il y en a des centaines. Cela permet notamment de profiter du 
		 * "batch fetching" d'hibernate (puisqu'on récupère dans un premier temps tous les proxies, et ensuite
		 * seulement on les initialise)
		 */
		transactionScopeIndependantRunnerService.run(true, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for (HistoryLogRunner<?> runner : runners) {
					runner.retrieveReference();
				}
				for (HistoryLogRunner<?> runner : runners) {
					runner.initializeReference();
				}
				return null;
			}
		});

		for (HistoryLogRunner<?> runner : runners) {
			runner.log();
		}
	}
	
	private static class HistoryLogRunner<T> {
		private final HistoryLogBeforeCommitWithDifferencesTask<T, ?, ?, ?> task;
		private Callable<T> referenceProvider;
		private T reference;
		
		public HistoryLogRunner(HistoryLogBeforeCommitWithDifferencesTask<T, ?, ?, ?> task) {
			super();
			this.task = task;
		}

		public void prepareRetrieval() {
			this.referenceProvider = task.getDifferenceGenerator().getReferenceProvider(task.getMainObject());
		}

		public void retrieveReference() {
			try {
				this.reference = referenceProvider.call();
			} catch (Exception e) {
				throw new IllegalStateException("Error retrieving a reference object for a diff", e);
			}
		}
		
		public void initializeReference() {
			try {
				task.getDifferenceGenerator().initializeReference(reference);
			} catch (Exception e) {
				throw new IllegalStateException("Error initializing a reference object for a diff", e);
			}
		}
		
		public void log() throws ServiceException, SecurityServiceException {
			task.logNow(reference);
		}
	}

}
