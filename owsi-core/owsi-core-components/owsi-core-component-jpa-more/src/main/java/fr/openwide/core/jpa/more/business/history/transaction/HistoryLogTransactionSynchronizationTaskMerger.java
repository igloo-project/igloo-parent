package fr.openwide.core.jpa.more.business.history.transaction;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.collections.CollectionUtils;
import fr.openwide.core.jpa.more.business.history.util.FactoredHistoryLogBeforeCommitWithDifferencesTask;
import fr.openwide.core.jpa.more.business.history.util.HistoryLogBeforeCommitWithDifferencesTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;
import fr.openwide.core.jpa.more.util.transaction.util.ITransactionSynchronizationTaskMerger;
import fr.openwide.core.spring.util.SpringBeanUtils;

@Component
public class HistoryLogTransactionSynchronizationTaskMerger implements ITransactionSynchronizationTaskMerger {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void merge(TransactionSynchronizationTasks tasks) {
		mergeHistoryLogBeforeCommitWithDifferences(tasks.getBeforeCommitTasks());
	}

	private void mergeHistoryLogBeforeCommitWithDifferences(List<ITransactionSynchronizationBeforeCommitTask> tasks) {
		if (!tasks.isEmpty()) {
			// We simply use .hashcode() and .equals() to remove the duplicates
			Set<ITransactionSynchronizationBeforeCommitTask> merged = Sets.newLinkedHashSet();
			Set<HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?, ?>> differenceTasks = Sets.newLinkedHashSet();
			for (ITransactionSynchronizationBeforeCommitTask task : tasks) {
				if (task instanceof HistoryLogBeforeCommitWithDifferencesTask) {
					differenceTasks.add((HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?, ?>)task);
				} else {
					merged.add(task);
				}
			}
			if (!differenceTasks.isEmpty()) {
				FactoredHistoryLogBeforeCommitWithDifferencesTask factoredTask
						= new FactoredHistoryLogBeforeCommitWithDifferencesTask(differenceTasks);
				SpringBeanUtils.autowireBean(applicationContext, factoredTask);
				merged.add(factoredTask);
			}
			CollectionUtils.replaceAll(tasks, merged);
		}
	}

}
