package org.iglooproject.jpa.more.business.history.transaction;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.more.business.history.util.FactoredHistoryLogBeforeCommitWithDifferencesTask;
import org.iglooproject.jpa.more.business.history.util.HistoryLogBeforeCommitWithDifferencesTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import org.iglooproject.jpa.more.util.transaction.model.TransactionSynchronizationTasks;
import org.iglooproject.jpa.more.util.transaction.util.ITransactionSynchronizationTaskMerger;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class HistoryLogTransactionSynchronizationTaskMerger
    implements ITransactionSynchronizationTaskMerger {

  @Autowired private ApplicationContext applicationContext;

  @Override
  public void merge(TransactionSynchronizationTasks tasks) {
    mergeHistoryLogBeforeCommitWithDifferences(tasks.getBeforeCommitTasks());
  }

  private void mergeHistoryLogBeforeCommitWithDifferences(
      List<ITransactionSynchronizationBeforeCommitTask> tasks) {
    if (!tasks.isEmpty()) {
      // We simply use .hashcode() and .equals() to remove the duplicates
      Set<ITransactionSynchronizationBeforeCommitTask> merged = Sets.newLinkedHashSet();
      Set<HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?, ?>> differenceTasks =
          Sets.newLinkedHashSet();
      for (ITransactionSynchronizationBeforeCommitTask task : tasks) {
        if (task instanceof HistoryLogBeforeCommitWithDifferencesTask) {
          differenceTasks.add((HistoryLogBeforeCommitWithDifferencesTask<?, ?, ?, ?, ?>) task);
        } else {
          merged.add(task);
        }
      }
      if (!differenceTasks.isEmpty()) {
        FactoredHistoryLogBeforeCommitWithDifferencesTask factoredTask =
            new FactoredHistoryLogBeforeCommitWithDifferencesTask(differenceTasks);
        SpringBeanUtils.autowireBean(applicationContext, factoredTask);
        merged.add(factoredTask);
      }
      CollectionUtils.replaceAll(tasks, merged);
    }
  }
}
