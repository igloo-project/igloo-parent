package org.iglooproject.jpa.more.business.task.util;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;

public enum TaskStatus {
  TO_RUN,
  RUNNING,
  COMPLETED,
  FAILED,
  INTERRUPTED, // Utilisé pour indiquer que la tâche est interrompue par arrêt technique de la file
  // ou du serveur (INTERRUPTED != FAILED)
  CANCELLED;

  public static final List<TaskStatus> CONSUMABLE_TASK_STATUS =
      List.of(TaskStatus.TO_RUN, TaskStatus.RUNNING, TaskStatus.FAILED, TaskStatus.INTERRUPTED);

  public static final List<TaskStatus> RELOADABLE_TASK_STATUS =
      List.of(TaskStatus.CANCELLED, TaskStatus.FAILED, TaskStatus.INTERRUPTED);

  public static final List<TaskStatus> CANCELLABLE_TASK_STATUS =
      List.of(TaskStatus.FAILED, TaskStatus.INTERRUPTED);

  public static List<String> getValuesAsStringList() {
    return Arrays.stream(values()).map(Object::toString).collect(ImmutableList.toImmutableList());
  }
}
