package fr.openwide.core.test.jpa.more.business.property;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyIdTemplate;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyIdTemplate;
import fr.openwide.core.jpa.more.business.property.service.IConfigurablePropertyService;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;

public class TestProperty extends AbstractJpaMoreTestCase {

	@Autowired
	private IConfigurablePropertyService propertyService;

	@Test
	public void mutableProperty() throws ServiceException, SecurityServiceException {
		MutablePropertyId<String> mutablePropertyString = new MutablePropertyId<>("mutable.property.string");
		propertyService.registerString(mutablePropertyString, "MyDefaultValue");
		Assert.assertEquals("MyDefaultValue", propertyService.get(mutablePropertyString));
		propertyService.set(mutablePropertyString, "MyValue");
		Assert.assertEquals("MyValue", propertyService.get(mutablePropertyString));
	}

	@Test
	public void immutableProperty() {
		ImmutablePropertyId<String> immutablePropertyString = new ImmutablePropertyId<>("property.string.value");
		propertyService.registerString(immutablePropertyString);
		Assert.assertEquals("MyValue", propertyService.get(immutablePropertyString));
	}

	@Test
	public void immutablePropertyTemplate() {
		ImmutablePropertyIdTemplate<String> immutablePropertyTemplate = new ImmutablePropertyIdTemplate<>("property.string.%1s");
		propertyService.registerString(immutablePropertyTemplate);
		Assert.assertEquals("MyValue", propertyService.get(immutablePropertyTemplate.create("value")));
	}

	@Test
	public void mutablePropertyTemplate() throws ServiceException, SecurityServiceException {
		MutablePropertyIdTemplate<Long> mutablePropertyTemplate = new MutablePropertyIdTemplate<>("property.long.%1s");
		propertyService.registerLong(mutablePropertyTemplate);
		MutablePropertyId<Long> mutableProperty = mutablePropertyTemplate.create("value");
		propertyService.set(mutableProperty, (Long) 1L);
		Assert.assertEquals((Long) 1L, propertyService.get(mutableProperty));
	}

	@Test
	public void immutablePropertyDate() throws ServiceException, SecurityServiceException {
		ImmutablePropertyId<Date> immutablePropertyIdDate = new ImmutablePropertyId<>("property.date.value");
		propertyService.registerDate(immutablePropertyIdDate);
		propertyService.get(immutablePropertyIdDate);
		
		ImmutablePropertyId<Date> immutablePropertyIdDateTime1 = new ImmutablePropertyId<>("property.dateTime.value");
		propertyService.registerDateTime(immutablePropertyIdDateTime1);
		propertyService.get(immutablePropertyIdDateTime1);
		
		ImmutablePropertyId<Date> immutablePropertyIdDateTime2 = new ImmutablePropertyId<>("property.dateTime.value2");
		propertyService.registerDateTime(immutablePropertyIdDateTime2, new Date());
		propertyService.get(immutablePropertyIdDateTime2);
		
		MutablePropertyIdTemplate<Date> mutablePropertyTemplateDate = new MutablePropertyIdTemplate<>("property.date.%1s");
		propertyService.registerDateTime(mutablePropertyTemplateDate, new Date());
		MutablePropertyId<Date> mutablePropertyDate = mutablePropertyTemplateDate.create("value");
		propertyService.get(mutablePropertyDate);
		propertyService.set(mutablePropertyDate, new Date());
		propertyService.get(mutablePropertyDate);
	}

}
