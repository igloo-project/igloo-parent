package test.jpa.more.business.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.exception.PropertyServiceDuplicateRegistrationException;
import org.iglooproject.spring.property.exception.PropertyServiceIncompleteRegistrationException;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.IMutablePropertyValueMap;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyValueMap;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.PropertyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestPropertyService {

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
	
	private static class PropertyIds extends AbstractPropertyIds {
		static final MutablePropertyId<String> MUTABLE_STRING = mutable("mutable.property.string");
		static final MutablePropertyId<Long> MUTABLE_LONG = mutable("mutable.property.long");
		static final MutablePropertyId<String> MUTABLE_STRING_WITH_DEFAULT = mutable("mutable.property.string.default");
	}
	
	private static class ImmutablePropertyIds extends AbstractPropertyIds {
		static final ImmutablePropertyId<String> IMMUTABLE_STRING = immutable("immutable.property.string");
		static final ImmutablePropertyId<Long> IMMUTABLE_LONG = immutable("immutable.property.long");
	}
	
	private void initPropertyService(IPropertyRegistryConfig registrationCallback) {
		propertyService.setPlatformTransactionManager(platformTransactionManager);
		propertyService.init(Collections.singleton(registrationCallback));
	}
	
	@Test
	void mutablePropertyGet() throws ServiceException, SecurityServiceException {
		initPropertyService(new IPropertyRegistryConfig() {
			@Override
			public void register(IPropertyRegistry propertyService) {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerLong(PropertyIds.MUTABLE_LONG);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
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
	void mutablePropertySet() throws ServiceException, SecurityServiceException {
		initPropertyService(new IPropertyRegistryConfig() {
			@Override
			public void register(IPropertyRegistry propertyService) {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerLong(PropertyIds.MUTABLE_LONG);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
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
	void mutablePropertySetAll() throws ServiceException, SecurityServiceException {
		initPropertyService(new IPropertyRegistryConfig() {
			@Override
			public void register(IPropertyRegistry propertyService) {
				propertyService.registerString(PropertyIds.MUTABLE_STRING);
				propertyService.registerLong(PropertyIds.MUTABLE_LONG);
				propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
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

	@Test
	void partialRegistration() {
		assertThrows(
			PropertyServiceIncompleteRegistrationException.class,
			() -> initPropertyService(new IPropertyRegistryConfig() {
				@Override
				public void register(IPropertyRegistry propertyService) {
					propertyService.registerString(PropertyIds.MUTABLE_STRING);
					propertyService.registerString(PropertyIds.MUTABLE_STRING_WITH_DEFAULT, "MyDefaultValue");
					// Do not register the last property
				}
			})
		);
	}

	@Test
	void mutablePropertyUnregistered() {
		// Do not register any property
		
		when(mutablePropertyDao.getInTransaction("mutable.property.string")).thenReturn("MyValue");
		
		assertThrows(
			PropertyServiceIncompleteRegistrationException.class,
			() -> propertyService.get(PropertyIds.MUTABLE_STRING)
		);
	}

	@Test
	void immutablePropertyGet() {
		initPropertyService(new IPropertyRegistryConfig() {
			@Override
			public void register(IPropertyRegistry propertyService) {
				propertyService.registerString(ImmutablePropertyIds.IMMUTABLE_STRING);
				propertyService.registerLong(ImmutablePropertyIds.IMMUTABLE_LONG);
			}
		});
		when(immutablePropertyDao.get("immutable.property.string")).thenReturn("MyValue");
		when(immutablePropertyDao.get("immutable.property.long")).thenReturn("1");
		
		assertEquals("MyValue", propertyService.get(ImmutablePropertyIds.IMMUTABLE_STRING));
		assertEquals((Long) 1L, propertyService.get(ImmutablePropertyIds.IMMUTABLE_LONG));
	}

	@Test
	void immutablePropertyUnregistered() {
		// Do not register any property
		
		when(immutablePropertyDao.get("immutable.property.string")).thenReturn("MyValue");
		
		assertThrows(
			PropertyServiceIncompleteRegistrationException.class,
			() -> propertyService.get(ImmutablePropertyIds.IMMUTABLE_STRING)
		);
	}
	
	private static class DuplicatePropertyIds extends AbstractPropertyIds {
		static final MutablePropertyId<String> MUTABLE_STRING = mutable(ImmutablePropertyIds.IMMUTABLE_STRING.getKey());
		static final ImmutablePropertyId<String> IMMUTABLE_STRING = immutable(ImmutablePropertyIds.IMMUTABLE_STRING.getKey());
	}

	@Test
	void propertyAlreadyRegistered() {
		assertThrows(
			PropertyServiceDuplicateRegistrationException.class,
			() -> initPropertyService(new IPropertyRegistryConfig() {
					@Override
					public void register(IPropertyRegistry propertyService) {
						propertyService.registerString(ImmutablePropertyIds.IMMUTABLE_STRING);
						propertyService.registerString(DuplicatePropertyIds.IMMUTABLE_STRING);
					}
				})
			);
	}
	
	public void propertiesMutableImmutableWithSameKey() {
		initPropertyService(new IPropertyRegistryConfig() {
			@Override
			public void register(IPropertyRegistry propertyService) {
				propertyService.registerString(DuplicatePropertyIds.IMMUTABLE_STRING);
				propertyService.registerString(DuplicatePropertyIds.MUTABLE_STRING);
			}
		});
	}

}
