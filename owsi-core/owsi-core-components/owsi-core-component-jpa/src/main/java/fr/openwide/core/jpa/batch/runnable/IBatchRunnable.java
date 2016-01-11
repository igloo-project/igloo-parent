package fr.openwide.core.jpa.batch.runnable;

import java.util.List;

public interface IBatchRunnable<E> {

	void preExecute();

	void executePartition(List<E> partition);

	void postExecute();
	
	void onError(Exception exception);
	
	Writeability getWriteability();

}