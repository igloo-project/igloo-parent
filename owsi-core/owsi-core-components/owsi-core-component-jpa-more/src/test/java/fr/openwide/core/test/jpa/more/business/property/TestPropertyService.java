package fr.openwide.core.test.jpa.more.business.property;

import org.junit.Assert;
import org.junit.Before;
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

	private static final String MUTABLE_PROPERTY_STRING_KEY = "mutable.property.string";
	private static final String MUTABLE_PROPERTY_STRING_VALUE = "MUTABLE_PROPERTY_STRING_VALUE";
	private static final String MUTABLE_PROPERTY_STRING_VALUE_AS_STRING = MUTABLE_PROPERTY_STRING_VALUE;
	private static final MutablePropertyId<String> MUTABLE_PROPERTY_STRING = new MutablePropertyId<>(MUTABLE_PROPERTY_STRING_KEY);
	
	private static final String MUTABLE_PROPERTY_LONG_KEY = "mutable.property.long";
	private static final Long MUTABLE_PROPERTY_LONG_VALUE = 1L;
	private static final String MUTABLE_PROPERTY_LONG_VALUE_AS_STRING = "1";
	private static final MutablePropertyId<Long> MUTABLE_PROPERTY_LONG = new MutablePropertyId<>(MUTABLE_PROPERTY_LONG_KEY);
	
	private static final String MUTABLE_PROPERTY_UNREGISTERED_STRING_KEY = "mutable.property.unregistered.string";
	private static final String MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE = "MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE";
	private static final String MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING = MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE;
	private static final MutablePropertyId<String> MUTABLE_PROPERTY_UNREGISTERED_STRING = new MutablePropertyId<>(MUTABLE_PROPERTY_UNREGISTERED_STRING_KEY);
	
	private static final String IMMUTABLE_PROPERTY_STRING_KEY = "immutable.property.string";
	private static final String IMMUTABLE_PROPERTY_STRING_VALUE = "IMMUTABLE_PROPERTY_STRING_VALUE";
	private static final String IMMUTABLE_PROPERTY_STRING_VALUE_AS_STRING = IMMUTABLE_PROPERTY_STRING_VALUE;
	private static final ImmutablePropertyId<String> IMMUTABLE_PROPERTY_STRING = new ImmutablePropertyId<>(IMMUTABLE_PROPERTY_STRING_KEY);
	
	private static final String IMMUTABLE_PROPERTY_LONG_KEY = "immutable.property.long";
	private static final Long IMMUTABLE_PROPERTY_LONG_VALUE = 1L;
	private static final String IMMUTABLE_PROPERTY_LONG_VALUE_AS_STRING = "1";
	private static final ImmutablePropertyId<Long> IMMUTABLE_PROPERTY_LONG = new ImmutablePropertyId<>(IMMUTABLE_PROPERTY_LONG_KEY);
	
	private static final String IMMUTABLE_PROPERTY_UNREGISTERED_STRING_KEY = "immutable.property.unregistered.string";
	private static final String IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE = "IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE";
	private static final String IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING = IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE;
	private static final ImmutablePropertyId<String> IMMUTABLE_PROPERTY_UNREGISTERED_STRING = new ImmutablePropertyId<>(IMMUTABLE_PROPERTY_UNREGISTERED_STRING_KEY);
	
	@Test
	public void mutableProperty() {
		propertyService.register(MUTABLE_PROPERTY_STRING);
		propertyService.register(MUTABLE_PROPERTY_LONG, Longs.stringConverter());
		
		Assert.assertEquals(MUTABLE_PROPERTY_STRING_VALUE, propertyService.get(MUTABLE_PROPERTY_STRING));
		Assert.assertEquals(MUTABLE_PROPERTY_LONG_VALUE, propertyService.get(MUTABLE_PROPERTY_LONG));
		
		propertyService.set(MUTABLE_PROPERTY_STRING, MUTABLE_PROPERTY_STRING_VALUE + "_test");
		propertyService.set(MUTABLE_PROPERTY_LONG, MUTABLE_PROPERTY_LONG_VALUE + 1);
	}

	@Test
	public void mutablePropertyUnregistered() {
		thrown.expect(IllegalStateException.class);
		propertyService.get(MUTABLE_PROPERTY_UNREGISTERED_STRING);
	}

	@Test
	public void immutableProperty() {
		propertyService.register(IMMUTABLE_PROPERTY_STRING);
		propertyService.register(IMMUTABLE_PROPERTY_LONG, Longs.stringConverter());
		
		Assert.assertEquals(IMMUTABLE_PROPERTY_STRING_VALUE, propertyService.get(IMMUTABLE_PROPERTY_STRING));
		Assert.assertEquals(IMMUTABLE_PROPERTY_LONG_VALUE, propertyService.get(IMMUTABLE_PROPERTY_LONG));
	}

	@Test
	public void immutablePropertyUnregistered() {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Aucun converter renseigné pour la propriété. Propriété non définie.");
		propertyService.get(IMMUTABLE_PROPERTY_UNREGISTERED_STRING);
	}

	@Before
	public void initMock() {
		Mockito.when(mutablePropertyDao.get(MUTABLE_PROPERTY_STRING_KEY)).thenReturn(MUTABLE_PROPERTY_STRING_VALUE_AS_STRING);
		Mockito.when(mutablePropertyDao.get(MUTABLE_PROPERTY_LONG_KEY)).thenReturn(MUTABLE_PROPERTY_LONG_VALUE_AS_STRING);
		Mockito.when(mutablePropertyDao.get(MUTABLE_PROPERTY_UNREGISTERED_STRING_KEY)).thenReturn(MUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING);
		Mockito.when(immutablePropertyDao.get(IMMUTABLE_PROPERTY_STRING_KEY)).thenReturn(IMMUTABLE_PROPERTY_STRING_VALUE_AS_STRING);
		Mockito.when(immutablePropertyDao.get(IMMUTABLE_PROPERTY_LONG_KEY)).thenReturn(IMMUTABLE_PROPERTY_LONG_VALUE_AS_STRING);
		Mockito.when(immutablePropertyDao.get(IMMUTABLE_PROPERTY_UNREGISTERED_STRING_KEY)).thenReturn(IMMUTABLE_PROPERTY_UNREGISTERED_STRING_VALUE_AS_STRING);
	}

}
