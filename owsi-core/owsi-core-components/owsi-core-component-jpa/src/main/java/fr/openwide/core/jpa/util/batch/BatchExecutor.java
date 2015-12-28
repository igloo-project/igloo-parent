package fr.openwide.core.jpa.util.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutor {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public HibernateBatch newHibernateBatch() {
		return applicationContext.getBean(HibernateBatch.class);
	}

}
