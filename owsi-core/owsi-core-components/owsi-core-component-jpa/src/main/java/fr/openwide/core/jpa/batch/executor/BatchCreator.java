package fr.openwide.core.jpa.batch.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BatchCreator {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public SimpleHibernateBatch newSimpleHibernateBatch() {
		return applicationContext.getBean(SimpleHibernateBatch.class);
	}
	
	public MultithreadedBatch newMultithreadedBatch() {
		return applicationContext.getBean(MultithreadedBatch.class);
	}

}
