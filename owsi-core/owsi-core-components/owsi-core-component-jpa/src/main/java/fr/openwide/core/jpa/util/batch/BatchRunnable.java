package fr.openwide.core.jpa.util.batch;

import java.util.List;

public abstract class BatchRunnable<E> {
	
	public void preExecute(List<Long> allIds) {
	}
	
	public void preExecutePartition(List<E> entities) {
	}
	
	public abstract void run(E entity);
	
	public void postExecutePartition(List<E> entities) {
	}
	
	public void postExecute(List<Long> allIds) {
	}

}
