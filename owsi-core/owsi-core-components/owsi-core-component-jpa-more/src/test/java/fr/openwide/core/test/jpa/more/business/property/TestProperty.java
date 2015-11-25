package fr.openwide.core.test.jpa.more.business.property;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.model.CompositeProperty;
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
	public void compositeProperty() {
		ImmutablePropertyId<String> property1 = new ImmutablePropertyId<>("property.string.value");
		propertyService.registerString(property1);
		ImmutablePropertyId<String> property2 = new ImmutablePropertyId<>("property.string.value");
		propertyService.registerString(property2);
		
		CompositeProperty<String, String, String> compositeProperty = new CompositeProperty<>(property1, property2);
		propertyService.register(compositeProperty, new Function<Pair<String, String>, String>() {
			@Override
			public String apply(Pair<String, String> input) {
				return input == null ? null : new StringBuilder().append(input.getValue0()).append("_").append(input.getValue1()).toString();
			}
		});
		
		Assert.assertEquals(new StringBuilder().append("MyValue").append("_").append("MyValue").toString(), propertyService.get(compositeProperty));
	}

}
