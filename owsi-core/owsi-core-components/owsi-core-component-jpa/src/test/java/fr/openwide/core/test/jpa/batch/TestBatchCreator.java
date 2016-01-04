package fr.openwide.core.test.jpa.batch;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.Joiners;
import fr.openwide.core.jpa.batch.executor.BatchCreator;
import fr.openwide.core.jpa.batch.executor.BatchRunnable;
import fr.openwide.core.jpa.batch.executor.MultithreadedBatch;
import fr.openwide.core.jpa.batch.executor.SimpleHibernateBatch;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.business.person.model.Person;

public class TestBatchCreator extends AbstractJpaCoreTestCase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestBatchCreator.class);

	@Autowired
	private BatchCreator batchCreator;
	
	@Test
	public void testSimpleHibernateBatch() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		SimpleHibernateBatch batch = batchCreator.newSimpleHibernateBatch();
		batch.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		batch.run(Person.class, ids, new BatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
			}
		});
	}
	
	@Test
	public void testMultithreadedBatch() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		MultithreadedBatch batch = batchCreator.newMultithreadedBatch();
		batch.batchSize(10).threads(4);
		batch.run(Person.class.getSimpleName(), ids, new BatchRunnable<Long>() {
			@Override
			public void preExecutePartition(List<Long> partition) {
				LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
			}
			
			@Override
			public void executeUnit(Long unit) {
				Person person = personService.getById(unit);
				person.setLastName(person.getLastName() + " updated " + person.getId());
				try {
					personService.update(person);
					LOGGER.warn("Updated: " + person.getDisplayName());
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
	}
}
