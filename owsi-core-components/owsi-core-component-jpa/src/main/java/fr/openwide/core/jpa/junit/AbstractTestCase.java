package fr.openwide.core.jpa.junit;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.util.EntityManagerUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	EntityManagerExecutionListener.class
})
public abstract class AbstractTestCase {
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;
	
	protected abstract void cleanAll() throws ServiceException, SecurityServiceException;
	
	@Before
	public void init() throws ServiceException, SecurityServiceException {
		cleanAll();
		checkEmptyDatabase();
	}
	
	@After
	public void close() throws ServiceException, SecurityServiceException {
		cleanAll();
		checkEmptyDatabase();
	}
	
	protected <E extends GenericEntity<Integer, E>> void testEntityStringFields(E entity, IGenericEntityService<Integer, E> service)
			throws ServiceException, SecurityServiceException {
		BeanWrapper source = PropertyAccessorFactory.forBeanPropertyAccess(entity);
		PropertyDescriptor[] fields = source.getPropertyDescriptors();
		
		// creationDate est la date de création de l'objet donc indépendant entre les deux objets
		String[] ignoredFields = { };
		for (PropertyDescriptor field : fields) {
			String fieldName = field.getName();
			if (source.isWritableProperty(fieldName) && String.class.isAssignableFrom(field.getPropertyType())) {
				if (!ArrayUtils.contains(ignoredFields, fieldName) && source.isReadableProperty(fieldName)) {
					source.setPropertyValue(fieldName, fieldName);
				}
			}
		}
		
		service.create(entity);
		
		for (PropertyDescriptor field : fields) {
			String fieldName = field.getName();
			if (source.isWritableProperty(fieldName) && String.class.isAssignableFrom(field.getPropertyType())) {
				if (!ArrayUtils.contains(ignoredFields, fieldName) && source.isReadableProperty(fieldName)) {
					Assert.assertEquals(fieldName, source.getPropertyValue(fieldName));
				}
			}
		}
	}
	
	private void checkEmptyDatabase() {
		CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
		@SuppressWarnings("rawtypes")
		CriteriaQuery<GenericEntity> cq = cb.createQuery(GenericEntity.class);
		cq.from(GenericEntity.class);
		
		@SuppressWarnings("rawtypes")
		List<GenericEntity> entities = entityManagerUtils.getEntityManager().createQuery(cq).getResultList();
		
		if (entities.size() > 0) {
			Assert.fail(String.format("Il reste des objets de type %1$s", entities.get(0).getClass().getSimpleName()));
		}
	}
	
	protected <E extends GenericEntity<?, ?>> List<E> listEntities(Class<E> clazz) {
		CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		cq.from(GenericEntity.class);
		
		return entityManagerUtils.getEntityManager().createQuery(cq).getResultList();
	}
	
	protected <E extends GenericEntity<?, ?>> Long countEntities(Class<E> clazz) {
		CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		@SuppressWarnings("rawtypes")
		Root<GenericEntity> root = cq.from(GenericEntity.class);
		cq.select(cb.count(root));
		
		return (Long) entityManagerUtils.getEntityManager().createQuery(cq).getSingleResult();
	}
	
	protected void assertDatesWithinXSeconds(Date date1, Date date2, Integer delayInSeconds) {
		Assert.assertTrue(Math.abs(date1.getTime() - date2.getTime()) < delayInSeconds * 1000);
	}

	/**
	 * Clear current session. Beware that all current entities will be detached
	 */
	protected void clearSession() {
		entityManagerUtils.getEntityManager().clear();
	}
	
	protected void sessionDetach(Object object) {
		entityManagerUtils.getEntityManager().detach(object);
	}

}
