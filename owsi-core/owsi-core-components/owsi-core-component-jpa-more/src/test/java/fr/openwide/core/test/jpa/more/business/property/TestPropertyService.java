package fr.openwide.core.test.jpa.more.business.property;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.property.dao.IImmutablePropertyDao;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.service.PropertyServiceImpl;

public class TestPropertyService {

	@Mock
	private IImmutablePropertyDao immutablePropertyDao;

	@Mock
	private IMutablePropertyDao mutablePropertyDao;

	@Mock
	private TransactionTemplate readOnlyTransactionTemplate;

	@Mock
	private TransactionTemplate writeTransactionTemplate;

	@InjectMocks
	private PropertyServiceImpl propertyService;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Test
	public void mutablePropertyMock() throws ServiceException, SecurityServiceException {
		MutablePropertyId<String> mutablePropertyString = new MutablePropertyId<>("mutable.property.string");
		MutablePropertyId<Long> mutablePropertyLong = new MutablePropertyId<>("mutable.property.long");
		MutablePropertyId<String> mutablePropertyStringDefault = new MutablePropertyId<>("mutable.property.string.default");
		
		propertyService.registerString(mutablePropertyString);
		propertyService.registerLong(mutablePropertyLong);
		propertyService.registerString(mutablePropertyStringDefault, "MyDefaultValue");
		
		Mockito.when(mutablePropertyDao.getInTransaction("mutable.property.string")).thenReturn("MyValue");
		Assert.assertEquals("MyValue", propertyService.get(mutablePropertyString));
		Mockito.when(mutablePropertyDao.getInTransaction("mutable.property.long")).thenReturn("1");
		Assert.assertEquals((Long) 1L, propertyService.get(mutablePropertyLong));
		Mockito.when(mutablePropertyDao.getInTransaction("mutable.property.string.default")).thenReturn("MyDefaultValue");
		Assert.assertEquals("MyDefaultValue", propertyService.get(mutablePropertyStringDefault));
		
		propertyService.set(mutablePropertyString, "MyValue2");
		propertyService.set(mutablePropertyLong, 2L);
		propertyService.set(mutablePropertyStringDefault, "MyValue3");
	}

	@Test(expected = IllegalStateException.class)
	public void mutablePropertyUnregistered() {
		MutablePropertyId<String> mutablePropertyUnregisteredString = new MutablePropertyId<>("mutable.property.unregistered.string");
		Mockito.when(mutablePropertyDao.getInTransaction("mutable.property.unregistered.string")).thenReturn("MyValue");
		propertyService.get(mutablePropertyUnregisteredString);
	}

	@Test
	public void immutablePropertyMock() {
		ImmutablePropertyId<String> immutablePropertyString = new ImmutablePropertyId<>("immutable.property.string");
		ImmutablePropertyId<Long> immutablePropertyLong = new ImmutablePropertyId<>("immutable.property.long");
		
		Mockito.when(immutablePropertyDao.get("immutable.property.string")).thenReturn("MyValue");
		Mockito.when(immutablePropertyDao.get("immutable.property.long")).thenReturn("1");
		
		propertyService.registerString(immutablePropertyString);
		propertyService.registerLong(immutablePropertyLong);
		
		Assert.assertEquals("MyValue", propertyService.get(immutablePropertyString));
		Assert.assertEquals((Long) 1L, propertyService.get(immutablePropertyLong));
	}

	@Test(expected = IllegalStateException.class)
	public void immutablePropertyUnregistered() {
		ImmutablePropertyId<String> immutablePropertyUnregisteredString = new ImmutablePropertyId<>("immutable.property.unregistered.string");
		Mockito.when(immutablePropertyDao.get("immutable.property.unregistered.string")).thenReturn("MyValue");
		propertyService.get(immutablePropertyUnregisteredString);
	}

}
