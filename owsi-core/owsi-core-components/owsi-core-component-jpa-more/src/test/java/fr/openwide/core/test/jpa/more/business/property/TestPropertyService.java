package fr.openwide.core.test.jpa.more.business.property;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.common.primitives.Longs;

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
	public void mutableProperty() {
		String MUTABLE_PROPERTY_STRING_KEY = "mutable.property.string";
		String MUTABLE_PROPERTY_STRING_VALUE = "MUTABLE_PROPERTY_STRING_VALUE";
		String MUTABLE_PROPERTY_STRING_VALUE_AS_STRING = MUTABLE_PROPERTY_STRING_VALUE;
		MutablePropertyId<String> MUTABLE_PROPERTY_STRING = new MutablePropertyId<>(MUTABLE_PROPERTY_STRING_KEY);
		
		String MUTABLE_PROPERTY_LONG_KEY = "mutable.property.long";
		Long MUTABLE_PROPERTY_LONG_VALUE = 1L;
		String MUTABLE_PROPERTY_LONG_VALUE_AS_STRING = "1";
		MutablePropertyId<Long> MUTABLE_PROPERTY_LONG = new MutablePropertyId<>(MUTABLE_PROPERTY_LONG_KEY);
		
		Mockito.when(mutablePropertyDao.get(MUTABLE_PROPERTY_STRING_KEY)).thenReturn(MUTABLE_PROPERTY_STRING_VALUE_AS_STRING);
		Mockito.when(mutablePropertyDao.get(MUTABLE_PROPERTY_LONG_KEY)).thenReturn(MUTABLE_PROPERTY_LONG_VALUE_AS_STRING);
		
		propertyService.register(MUTABLE_PROPERTY_STRING);
		propertyService.register(MUTABLE_PROPERTY_LONG, Longs.stringConverter());
		
		Assert.assertEquals(MUTABLE_PROPERTY_STRING_VALUE, propertyService.get(MUTABLE_PROPERTY_STRING));
		Assert.assertEquals(MUTABLE_PROPERTY_LONG_VALUE, propertyService.get(MUTABLE_PROPERTY_LONG));
		
		propertyService.set(MUTABLE_PROPERTY_STRING, MUTABLE_PROPERTY_STRING_VALUE + "_test");
		propertyService.set(MUTABLE_PROPERTY_LONG, MUTABLE_PROPERTY_LONG_VALUE + 1);
	}

	@Test(expected = IllegalStateException.class)
	public void mutablePropertyUnregistered() {
		String MUTABLE_PROPERTY_UNREGISTERED_STRING_KEY = "mutable.property.unregistered.string";
		String MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE = "MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE";
		String MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING = MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE;
		MutablePropertyId<String> MUTABLE_PROPERTY_UNREGISTERED_STRING = new MutablePropertyId<>(MUTABLE_PROPERTY_UNREGISTERED_STRING_KEY);
		
		Mockito.when(mutablePropertyDao.get(MUTABLE_PROPERTY_UNREGISTERED_STRING_KEY)).thenReturn(MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING);
		propertyService.get(MUTABLE_PROPERTY_UNREGISTERED_STRING);
	}

	@Test
	public void immutableProperty() {
		String IMMUTABLE_PROPERTY_STRING_KEY = "immutable.property.string";
		String IMMUTABLE_PROPERTY_STRING_VALUE = "IMMUTABLE_PROPERTY_STRING_VALUE";
		String IMMUTABLE_PROPERTY_STRING_VALUE_AS_STRING = IMMUTABLE_PROPERTY_STRING_VALUE;
		ImmutablePropertyId<String> IMMUTABLE_PROPERTY_STRING = new ImmutablePropertyId<>(IMMUTABLE_PROPERTY_STRING_KEY);
		
		String IMMUTABLE_PROPERTY_LONG_KEY = "immutable.property.long";
		Long IMMUTABLE_PROPERTY_LONG_VALUE = 1L;
		String IMMUTABLE_PROPERTY_LONG_VALUE_AS_STRING = "1";
		ImmutablePropertyId<Long> IMMUTABLE_PROPERTY_LONG = new ImmutablePropertyId<>(IMMUTABLE_PROPERTY_LONG_KEY);
		
		Mockito.when(immutablePropertyDao.get(IMMUTABLE_PROPERTY_STRING_KEY)).thenReturn(IMMUTABLE_PROPERTY_STRING_VALUE_AS_STRING);
		Mockito.when(immutablePropertyDao.get(IMMUTABLE_PROPERTY_LONG_KEY)).thenReturn(IMMUTABLE_PROPERTY_LONG_VALUE_AS_STRING);
		
		propertyService.register(IMMUTABLE_PROPERTY_STRING);
		propertyService.register(IMMUTABLE_PROPERTY_LONG, Longs.stringConverter());
		
		Assert.assertEquals(IMMUTABLE_PROPERTY_STRING_VALUE, propertyService.get(IMMUTABLE_PROPERTY_STRING));
		Assert.assertEquals(IMMUTABLE_PROPERTY_LONG_VALUE, propertyService.get(IMMUTABLE_PROPERTY_LONG));
	}

	@Test(expected = IllegalStateException.class)
	public void immutablePropertyUnregistered() {
		String IMMUTABLE_PROPERTY_UNREGISTERED_STRING_KEY = "immutable.property.unregistered.string";
		String IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE = "IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE";
		String IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING = IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE;
		ImmutablePropertyId<String> IMMUTABLE_PROPERTY_UNREGISTERED_STRING = new ImmutablePropertyId<>(IMMUTABLE_PROPERTY_UNREGISTERED_STRING_KEY);
		
		Mockito.when(immutablePropertyDao.get(IMMUTABLE_PROPERTY_UNREGISTERED_STRING_KEY)).thenReturn(IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING);
		
		propertyService.get(IMMUTABLE_PROPERTY_UNREGISTERED_STRING);
	}

}
