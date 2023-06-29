package test.jpa.more.business;

import jakarta.persistence.Query;
import test.jpa.more.config.spring.SpringBootTestJpaMore;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

@SpringBootTestJpaMore
class QueuedTaskHolderServiceTest extends AbstractJpaMoreTestCase {

	@Test
	void testGetListConsumableOk() throws ServiceException, SecurityServiceException {
		Query query = getEntityManager().createQuery("select queuedTaskHolder\n" + 
				"from QueuedTaskHolder queuedTaskHolder\n" + 
				"where queuedTaskHolder.status in (:statuses) and queuedTaskHolder.queueId = :queue\n" + 
				"order by queuedTaskHolder.id asc").setParameter("statuses", ImmutableList.of(TaskStatus.CANCELLED, TaskStatus.COMPLETED)).setParameter("queue", "queue");
		query.getResultList();
	}

	@Test
	void testGetListConsumableKo() throws ServiceException, SecurityServiceException {
		Query query = getEntityManager().createQuery("select queuedTaskHolder\n" + 
				"from QueuedTaskHolder queuedTaskHolder\n" + 
				"where queuedTaskHolder.status in (?1) and queuedTaskHolder.queueId = ?2\n" + 
				"order by queuedTaskHolder.id asc").setParameter(1, ImmutableList.of(TaskStatus.CANCELLED, TaskStatus.COMPLETED)).setParameter(2, "queue");
		query.getResultList();
	}

}
