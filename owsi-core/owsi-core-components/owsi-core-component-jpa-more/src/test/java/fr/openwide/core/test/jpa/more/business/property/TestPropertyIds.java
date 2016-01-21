package fr.openwide.core.test.jpa.more.business.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.IPropertyRegistryKey;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.ImmutablePropertyIdTemplate;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyIdTemplate;

public class TestPropertyIds {

	private static class PropertyIds extends AbstractPropertyIds {
		static final MutablePropertyId<String> MUTABLE = mutable("mutable.property");
		static final MutablePropertyIdTemplate<String> MUTABLE_TEMPLATE = mutableTemplate("mutable.property.template.%s");
		static final ImmutablePropertyId<String> IMMUTABLE = immutable("immutable.property");
		static final ImmutablePropertyIdTemplate<String> IMMUTABLE_TEMPLATE = immutableTemplate("immutable.property.template.%s");
	}
	
	private static final Set<IPropertyRegistryKey<?>> ALL_KEYS = ImmutableSet.<IPropertyRegistryKey<?>>of(
			PropertyIds.MUTABLE, PropertyIds.MUTABLE_TEMPLATE,
			PropertyIds.IMMUTABLE, PropertyIds.IMMUTABLE_TEMPLATE
	);
	
	@Test
	public void mutableProperty() {
		assertEquals("mutable.property", PropertyIds.MUTABLE.getKey());
		assertEquals(PropertyIds.class, PropertyIds.MUTABLE.getDeclaration().getDeclaringClass());
		assertEquals(ALL_KEYS, PropertyIds.MUTABLE.getDeclaration().getDeclaredKeys());
	}

	@Test
	public void mutablePropertyTemplate() {
		assertEquals("mutable.property.template.%s", PropertyIds.MUTABLE_TEMPLATE.getFormat());
		assertEquals(PropertyIds.class, PropertyIds.MUTABLE_TEMPLATE.getDeclaration().getDeclaringClass());
		assertEquals(ALL_KEYS, PropertyIds.MUTABLE_TEMPLATE.getDeclaration().getDeclaredKeys());
		
		MutablePropertyId<String> generatedProperty = PropertyIds.MUTABLE_TEMPLATE.create("TEST");
		assertEquals("mutable.property.template.TEST", generatedProperty.getKey());
		assertNull(generatedProperty.getDeclaration());
	}
	
	@Test
	public void immutableProperty() {
		assertEquals("immutable.property", PropertyIds.IMMUTABLE.getKey());
		assertEquals(PropertyIds.class, PropertyIds.IMMUTABLE.getDeclaration().getDeclaringClass());
		assertEquals(ALL_KEYS, PropertyIds.IMMUTABLE.getDeclaration().getDeclaredKeys());
	}

	@Test
	public void immutablePropertyTemplate() {
		assertEquals("immutable.property.template.%s", PropertyIds.IMMUTABLE_TEMPLATE.getFormat());
		assertEquals(PropertyIds.class, PropertyIds.IMMUTABLE_TEMPLATE.getDeclaration().getDeclaringClass());
		assertEquals(ALL_KEYS, PropertyIds.IMMUTABLE_TEMPLATE.getDeclaration().getDeclaredKeys());
		
		ImmutablePropertyId<String> generatedProperty = PropertyIds.IMMUTABLE_TEMPLATE.create("TEST");
		assertEquals("immutable.property.template.TEST", generatedProperty.getKey());
		assertNull(generatedProperty.getDeclaration());
	}

}
