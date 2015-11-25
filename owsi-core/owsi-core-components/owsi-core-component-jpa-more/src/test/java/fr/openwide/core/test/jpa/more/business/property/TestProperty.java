package fr.openwide.core.test.jpa.more.business.property;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.service.IConfigurablePropertyService;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;

public class TestProperty extends AbstractJpaMoreTestCase {

	@Autowired
	private IConfigurablePropertyService propertyService;

	@Test
	public void mutableProperty() throws ServiceException, SecurityServiceException {
		MutablePropertyId<String> mutablePropertyString = new MutablePropertyId<>("mutable.property.string");
		propertyService.register(mutablePropertyString, "MyDefaultValue");
		Assert.assertEquals("MyDefaultValue", propertyService.get(mutablePropertyString));
		propertyService.set(mutablePropertyString, "MyValue");
		Assert.assertEquals("MyValue", propertyService.get(mutablePropertyString));
	}

	@Test
	public void immutableProperty() {
		ImmutablePropertyId<String> immutablePropertyString = new ImmutablePropertyId<>("property.string.value");
		propertyService.register(immutablePropertyString);
		Assert.assertEquals("MyValue", propertyService.get(immutablePropertyString));
	}

}
