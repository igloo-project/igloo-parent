package org.iglooproject.jpa.batch.runnable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IBatchRunnable<E> {

  void preExecute();

  void executePartition(List<E> partition);

  void postExecute();

  void onError(ExecutionException exception);

  Writeability getWriteability();
}
