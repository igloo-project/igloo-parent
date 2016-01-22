package fr.openwide.core.test.jpa.more.business.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	@SuppressWarnings("unchecked")
	private static <T> T serializeAndDeserialize(T object) {
		byte[] array;
		
		try {
			ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(arrayOut);
			objectOut.writeObject(object);
			array = arrayOut.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error while serializing " + object, e);
		}

		try {
			ByteArrayInputStream arrayIn = new ByteArrayInputStream(array);
			ObjectInputStream objectIn = new ObjectInputStream(arrayIn);
			return (T) objectIn.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Error while deserializing " + object, e);
		}
	}

	/**
	 * Check that the declaration doesn't get cloned by serialization + deserialization
	 */
	private static void checkSerialization(IPropertyRegistryKey<?> key) {
		IPropertyRegistryKey<?> serializedDeserialized = serializeAndDeserialize(key);
		assertNotSame(key, serializedDeserialized);
		assertEquals(key, serializedDeserialized);
		assertSame(key.getDeclaration(), serializedDeserialized.getDeclaration());
	}
	
	@Test
	public void serialization() {
		checkSerialization(PropertyIds.MUTABLE);
		checkSerialization(PropertyIds.IMMUTABLE);
		
		checkSerialization(PropertyIds.MUTABLE_TEMPLATE);
		checkSerialization(PropertyIds.IMMUTABLE_TEMPLATE);
		
		checkSerialization(PropertyIds.IMMUTABLE_TEMPLATE.create("TEST"));
		checkSerialization(PropertyIds.MUTABLE_TEMPLATE.create("TEST"));
	}
	
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
