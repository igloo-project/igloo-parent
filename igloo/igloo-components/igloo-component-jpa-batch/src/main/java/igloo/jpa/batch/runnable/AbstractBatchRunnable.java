package igloo.jpa.batch.runnable;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * An abstract base for batch runnables.
 * <p>This class is package-protected: use either {@link ReadOnlyBatchRunnable} or
 * {@link ReadWriteBatchRunnable} as as superclass for your implementation. Or,
 * alternatively, implement {@link IBatchRunnable} directly.
 */
/*package-protected*/ abstract class AbstractBatchRunnable<E> implements IBatchRunnable<E> {
	
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
	public void onError(ExecutionException exception) {
		throw new IllegalStateException(String.format("Unhandled exception when running %s", this), exception);
	}

}
