package fr.openwide.core.jpa.batch.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutorCreator {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public SimpleHibernateBatchExecutor newSimpleHibernateBatchExecutor() {
		return applicationContext.getBean(SimpleHibernateBatchExecutor.class);
	}
	
	public MultithreadedBatchExecutor newMultithreadedBatchExecutor() {
		return applicationContext.getBean(MultithreadedBatchExecutor.class);
	}

}
