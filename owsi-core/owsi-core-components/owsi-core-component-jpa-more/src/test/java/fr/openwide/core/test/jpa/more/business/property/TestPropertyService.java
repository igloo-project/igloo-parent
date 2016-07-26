package fr.openwide.core.test.jpa.more.business.property;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.spring.event.PropertyRegistryInitEvent;
import fr.openwide.core.spring.property.dao.IImmutablePropertyDao;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.exception.PropertyServiceDuplicateRegistrationException;
import fr.openwide.core.spring.property.exception.PropertyServiceIncompleteRegistrationException;
import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.IMutablePropertyValueMap;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyValueMap;
import fr.openwide.core.spring.property.service.PropertyServiceImpl;

public class TestPropertyService {

	@Mock
	private IImmutablePropertyDao immutablePropertyDao;

	@Mock
	private IMutablePropertyDao mutablePropertyDao;

	@Mock
	private PlatformTransactionManager platformTransactionManager;
	
	@Mock
	private ApplicationEventPublisher publisher;

	@InjectMocks
	private PropertyServiceImpl propertyService;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	private static class PropertyIds extends AbstractPropertyIds {
		static final MutablePropertyId<String> MUTABLE_STRING = mutable("mutable.property.string");
		static final MutablePropertyId<Long> MUTABLE_LONG = mutable("mutable.property.long");
		static final MutablePropertyId<String> MUTABLE_STRING_WITH_DEFAULT = mutable("mutable.property.string.default");
	}
	
	private static class ImmutablePropertyIds extends AbstractPropertyIds {
		static final ImmutablePropertyId<String> IMMUTABLE_STRING = immutable("immutable.property.string");
		static final ImmutablePropertyId<Long> IMMUTABLE_LONG = immutable("immutable.property.long");
	}
	
	private void initPropertyService(Answer<Void> registrationCallback) {
		propertyService.setApplicationEventPublisher(publisher);
		propertyService.setPlatformTransactionManager(platformTransactionManager);
		doAnswer(registrationCallback).when(publisher).publishEvent(Matchers.any(PropertyRegistryInitEvent.class));
		propertyService.init();
	}
	
	@Test
	public void mutablePropertyGet() throws ServiceException, SecurityServiceException {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerLong(PropertyIds.MUTABLE_LONG);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
				return null;
			}
		});
		
		when(mutablePropertyDao.getInTransaction("mutable.property.string")).thenReturn("MyValue");
		when(mutablePropertyDao.getInTransaction("mutable.property.long")).thenReturn("1");
		when(mutablePropertyDao.getInTransaction("mutable.property.string.default")).thenReturn("MyDefaultValue");
		
		assertEquals("MyValue", propertyService.get(PropertyIds.MUTABLE_STRING));
		assertEquals((Long) 1L, propertyService.get(PropertyIds.MUTABLE_LONG));
		assertEquals("MyDefaultValue", propertyService.get(PropertyIds.MUTABLE_STRING_WITH_DEFAULT));
	}

	@Test
	public void mutablePropertySet() throws ServiceException, SecurityServiceException {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerLong(PropertyIds.MUTABLE_LONG);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
				return null;
			}
		});
		
		propertyService.set(PropertyIds.MUTABLE_STRING, "MyValue2");
		propertyService.set(PropertyIds.MUTABLE_LONG, 2L);
		propertyService.set(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyValue3");
		
		verify(mutablePropertyDao).setInTransaction("mutable.property.string", "MyValue2");
		verify(mutablePropertyDao).setInTransaction("mutable.property.long", "2");
		verify(mutablePropertyDao).setInTransaction("mutable.property.string.default", "MyValue3");
	}

	@Test
	public void mutablePropertySetAll() throws ServiceException, SecurityServiceException {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerLong(PropertyIds.MUTABLE_LONG);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
				return null;
			}
		});
		
		IMutablePropertyValueMap propertyValueMap = new MutablePropertyValueMap();
		
		propertyValueMap.put(PropertyIds.MUTABLE_STRING, "MyValue2");
		propertyValueMap.put(PropertyIds.MUTABLE_LONG, 2L);
		propertyValueMap.put(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyValue3");
		
		propertyService.setAll(propertyValueMap);
		
		verify(mutablePropertyDao).setInTransaction("mutable.property.string", "MyValue2");
		verify(mutablePropertyDao).setInTransaction("mutable.property.long", "2");
		verify(mutablePropertyDao).setInTransaction("mutable.property.string.default", "MyValue3");
	}

	@Test(expected = PropertyServiceIncompleteRegistrationException.class)
	public void partialRegistration() {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
				// Do not register the last property
				return null;
			}
		});
	}

	@Test(expected = PropertyServiceIncompleteRegistrationException.class)
	public void mutablePropertyUnregistered() {
		// Do not register any property
		
		when(mutablePropertyDao.getInTransaction("mutable.property.string")).thenReturn("MyValue");
		
		propertyService.get(PropertyIds.MUTABLE_STRING);
	}

	@Test
	public void immutablePropertyGet() {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(ImmutablePropertyIds.IMMUTABLE_STRING);
				propertyService.registerLong(ImmutablePropertyIds.IMMUTABLE_LONG);
				return null;
			}
		});
		when(immutablePropertyDao.get("immutable.property.string")).thenReturn("MyValue");
		when(immutablePropertyDao.get("immutable.property.long")).thenReturn("1");
		
		assertEquals("MyValue", propertyService.get(ImmutablePropertyIds.IMMUTABLE_STRING));
		assertEquals((Long) 1L, propertyService.get(ImmutablePropertyIds.IMMUTABLE_LONG));
	}

	@Test(expected = PropertyServiceIncompleteRegistrationException.class)
	public void immutablePropertyUnregistered() {
		// Do not register any property
		
		when(immutablePropertyDao.get("immutable.property.string")).thenReturn("MyValue");
		
		propertyService.get(ImmutablePropertyIds.IMMUTABLE_STRING);
	}
	
	private static class DuplicatePropertyIds extends AbstractPropertyIds {
		static final MutablePropertyId<String> MUTABLE_STRING = mutable(ImmutablePropertyIds.IMMUTABLE_STRING.getKey());
		static final ImmutablePropertyId<String> IMMUTABLE_STRING = immutable(ImmutablePropertyIds.IMMUTABLE_STRING.getKey());
	}

	@Test(expected = PropertyServiceDuplicateRegistrationException.class)
	public void propertyAlreadyRegistered() {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(ImmutablePropertyIds.IMMUTABLE_STRING);
				propertyService.registerString(DuplicatePropertyIds.IMMUTABLE_STRING);
				return null;
			}
		});
	}
	
	public void propertiesMutableImmutableWithSameKey() {
		initPropertyService(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				propertyService.registerString(DuplicatePropertyIds.IMMUTABLE_STRING);
				propertyService.registerString(DuplicatePropertyIds.MUTABLE_STRING);
				return null;
			}
		});
	}

}
