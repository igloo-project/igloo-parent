package fr.openwide.core.jpa.batch.executor;

import java.util.List;

public abstract class BatchRunnable<E> {

	public void preExecute(List<Long> allIds) {
	}

	public void executePartition(List<E> partition) {
		preExecutePartition(partition);

		for (E entity : partition) {
			executeUnit(entity);
		}

		postExecutePartition(partition);
	}

	public void preExecutePartition(List<E> partition) {
	}

	public void executeUnit(E unit) {
	}

	public void postExecutePartition(List<E> partition) {
	}
	
	public void postExecute(List<Long> allIds) {
	}

}
