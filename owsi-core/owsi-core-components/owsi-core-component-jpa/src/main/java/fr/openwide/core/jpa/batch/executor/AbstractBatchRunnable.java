package fr.openwide.core.jpa.batch.executor;

import java.util.List;

public abstract class AbstractBatchRunnable<E> implements IBatchRunnable<E> {
	
	@Override
	public void preExecute() {
	}

	@Override
	public void executePartition(List<E> partition) {
		preExecutePartition(partition);

		for (E entity : partition) {
			executeUnit(entity);
		}

		postExecutePartition(partition);
	}

	protected void preExecutePartition(List<E> partition) {
	}

	protected void executeUnit(E unit) {
	}

	protected void postExecutePartition(List<E> partition) {
	}
	
	@Override
	public void postExecute() {
	}
	
	@Override
	public void onError(Exception exception) {
		throw new IllegalStateException(String.format("Unhandled error when running %s", this), exception);
	}

}
