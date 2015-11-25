package fr.openwide.core.test.jpa.more.business.property;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.common.primitives.Longs;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.dao.IMutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.service.PropertyServiceImpl;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;

public class TestPropertyService extends AbstractJpaMoreTestCase {

	@Mock
	private IImmutablePropertyDao immutablePropertyDao;

	@Mock
	private IMutablePropertyDao mutablePropertyDao;

	@InjectMocks
	private PropertyServiceImpl propertyService;

	@Test
	public void mutablePropertyMock() throws ServiceException, SecurityServiceException {
		MutablePropertyId<String> mutablePropertyString = new MutablePropertyId<>("mutable.property.string");
		MutablePropertyId<Long> mutablePropertyLong = new MutablePropertyId<>("mutable.property.long");
		MutablePropertyId<String> mutablePropertyStringDefault = new MutablePropertyId<>("mutable.property.string.default");
		
		Mockito.when(mutablePropertyDao.get("mutable.property.string")).thenReturn("MyValue");
		Mockito.when(mutablePropertyDao.get("mutable.property.long")).thenReturn("1");
		Mockito.when(mutablePropertyDao.get("mutable.property.string.default")).thenReturn(null);
		
		propertyService.register(mutablePropertyString);
		propertyService.register(mutablePropertyLong, Longs.stringConverter());
		propertyService.register(mutablePropertyStringDefault, "MyDefaultValue");
		
		Assert.assertEquals("MyValue", propertyService.get(mutablePropertyString));
		Assert.assertEquals((Long) 1L, propertyService.get(mutablePropertyLong));
		Assert.assertEquals("MyDefaultValue", propertyService.get(mutablePropertyStringDefault));
		
		propertyService.set(mutablePropertyString, "MyValue2");
		propertyService.set(mutablePropertyLong, 2L);
		propertyService.set(mutablePropertyStringDefault, "MyValue3");
	}

	@Test(expected = IllegalStateException.class)
	public void mutablePropertyUnregistered() {
		MutablePropertyId<String> mutablePropertyUnregisteredString = new MutablePropertyId<>("mutable.property.unregistered.string");
		Mockito.when(mutablePropertyDao.get("mutable.property.unregistered.string")).thenReturn("MyValue");
		propertyService.get(mutablePropertyUnregisteredString);
	}

	@Test
	public void immutablePropertyMock() {
		ImmutablePropertyId<String> immutablePropertyString = new ImmutablePropertyId<>("immutable.property.string");
		ImmutablePropertyId<Long> immutablePropertyLong = new ImmutablePropertyId<>("immutable.property.long");
		
		Mockito.when(immutablePropertyDao.get("immutable.property.string")).thenReturn("MyValue");
		Mockito.when(immutablePropertyDao.get("immutable.property.long")).thenReturn("1");
		
		propertyService.register(immutablePropertyString);
		propertyService.register(immutablePropertyLong, Longs.stringConverter());
		
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
