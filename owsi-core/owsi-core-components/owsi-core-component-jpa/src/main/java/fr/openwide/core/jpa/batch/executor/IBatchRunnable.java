package fr.openwide.core.jpa.batch.executor;

import java.util.List;

public interface IBatchRunnable<E> {

	void preExecute(List<Long> allIds);

	void executePartition(List<E> partition);

	void postExecute(List<Long> allIds);

}