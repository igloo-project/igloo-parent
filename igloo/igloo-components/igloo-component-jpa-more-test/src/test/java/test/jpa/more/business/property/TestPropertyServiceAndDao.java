package test.jpa.more.business.property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.IMutablePropertyValueMap;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.model.ImmutablePropertyIdTemplate;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyIdTemplate;
import org.iglooproject.spring.property.model.MutablePropertyValueMap;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import test.jpa.more.business.AbstractJpaMoreTestCase;
import test.jpa.more.business.property.TestPropertyServiceAndDao.TestPropertyServiceAndDaoConfig;

@ContextConfiguration(classes = TestPropertyServiceAndDaoConfig.class)
class TestPropertyServiceAndDao extends AbstractJpaMoreTestCase {

  public static class TestPropertyServiceAndDaoConfig
      extends AbstractApplicationPropertyRegistryConfig {

    @Override
    public void register(IPropertyRegistry registry) {
      registry.registerString(PropertyIds.MUTABLE_STRING, "MyDefaultValue");
      registry.registerString(PropertyIds.IMMUTABLE_STRING);
      registry.registerString(PropertyIds.IMMUTABLE_STRING_TEMPLATE);
      registry.registerLong(PropertyIds.MUTABLE_LONG_TEMPLATE);
      registry.registerDate(PropertyIds.IMMUTABLE_DATE);
      registry.registerDateTime(PropertyIds.IMMUTABLE_DATETIME);
      registry.registerDateTime(PropertyIds.IMMUTABLE_DATETIME_WITH_DEFAULT, new Date());
      registry.registerDateTime(PropertyIds.MUTABLE_DATETIME_TEMPLATE, new Date());
    }
  }

  private static class PropertyIds extends AbstractPropertyIds {
    static final MutablePropertyId<String> MUTABLE_STRING = mutable("mutable.property.string");
    static final ImmutablePropertyId<String> IMMUTABLE_STRING = immutable("property.string.value");
    static final ImmutablePropertyIdTemplate<String> IMMUTABLE_STRING_TEMPLATE =
        immutableTemplate("property.string.%1s");
    static final MutablePropertyIdTemplate<Long> MUTABLE_LONG_TEMPLATE =
        mutableTemplate("property.long.%1s");
    static final ImmutablePropertyId<Date> IMMUTABLE_DATE = immutable("property.date.value");
    static final ImmutablePropertyId<Date> IMMUTABLE_DATETIME =
        immutable("property.dateTime.value");
    static final ImmutablePropertyId<Date> IMMUTABLE_DATETIME_WITH_DEFAULT =
        immutable("property.dateTime.value2");
    static final MutablePropertyIdTemplate<Date> MUTABLE_DATETIME_TEMPLATE =
        mutableTemplate("property.date.%1s");
  }

  @Test
  void mutableProperty() throws ServiceException, SecurityServiceException {
    assertEquals("MyDefaultValue", propertyService.get(PropertyIds.MUTABLE_STRING));

    propertyService.set(PropertyIds.MUTABLE_STRING, "MyValue");
    assertEquals("MyValue", propertyService.get(PropertyIds.MUTABLE_STRING));

    IMutablePropertyValueMap propertyValueMap = new MutablePropertyValueMap();
    propertyValueMap.put(PropertyIds.MUTABLE_STRING, "MyValue2");
    propertyService.setAll(propertyValueMap);
    assertEquals("MyValue2", propertyService.get(PropertyIds.MUTABLE_STRING));
  }

  @Test
  void immutableProperty() {
    assertEquals("MyValue", propertyService.get(PropertyIds.IMMUTABLE_STRING));
  }

  @Test
  void immutablePropertyTemplate() {
    assertEquals(
        "MyValue", propertyService.get(PropertyIds.IMMUTABLE_STRING_TEMPLATE.create("value")));
  }

  @Test
  void mutablePropertyTemplate() throws ServiceException, SecurityServiceException {
    MutablePropertyId<Long> mutableProperty = PropertyIds.MUTABLE_LONG_TEMPLATE.create("value");
    propertyService.set(mutableProperty, (Long) 1L);
    assertEquals((Long) 1L, propertyService.get(mutableProperty));
  }

  @Test
  void propertyDate() throws ServiceException, SecurityServiceException {
    propertyService.get(PropertyIds.IMMUTABLE_DATE);

    propertyService.get(PropertyIds.IMMUTABLE_DATETIME);

    propertyService.get(PropertyIds.IMMUTABLE_DATETIME_WITH_DEFAULT);

    MutablePropertyId<Date> mutablePropertyDate =
        PropertyIds.MUTABLE_DATETIME_TEMPLATE.create("value");
    propertyService.get(mutablePropertyDate);
    propertyService.set(mutablePropertyDate, new Date());
    propertyService.get(mutablePropertyDate);
  }
}
