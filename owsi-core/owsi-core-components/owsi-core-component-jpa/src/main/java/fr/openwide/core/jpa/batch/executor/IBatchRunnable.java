package fr.openwide.core.jpa.batch.executor;

import java.util.List;

public interface IBatchRunnable<E> {

	void preExecute();

	void executePartition(List<E> partition);

	void postExecute();
	
	void onError(Exception exception);

}