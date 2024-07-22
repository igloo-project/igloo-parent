package test.jpa.more.business.property;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableSet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import org.hamcrest.CoreMatchers;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.IPropertyRegistryKey;
import org.iglooproject.spring.property.model.IPropertyRegistryKeyDeclaration;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.model.ImmutablePropertyIdTemplate;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyIdTemplate;
import org.junit.jupiter.api.Test;

class TestPropertyIds {

  private static class PropertyIds extends AbstractPropertyIds {
    static final MutablePropertyId<String> MUTABLE = mutable("mutable.property");
    static final MutablePropertyIdTemplate<String> MUTABLE_TEMPLATE =
        mutableTemplate("mutable.property.template.%s");
    static final ImmutablePropertyId<String> IMMUTABLE = immutable("immutable.property");
    static final ImmutablePropertyIdTemplate<String> IMMUTABLE_TEMPLATE =
        immutableTemplate("immutable.property.template.%s");
  }

  private static class OtherPropertyIds extends AbstractPropertyIds {
    static final ImmutablePropertyId<String> IMMUTABLE = immutable("immutable.property");
  }

  private static final Set<IPropertyRegistryKey<?>> ALL_KEYS =
      ImmutableSet.<IPropertyRegistryKey<?>>of(
          PropertyIds.MUTABLE, PropertyIds.MUTABLE_TEMPLATE,
          PropertyIds.IMMUTABLE, PropertyIds.IMMUTABLE_TEMPLATE);

  @SuppressWarnings("unchecked")
  private static <T> T serializeAndDeserialize(T object) {
    byte[] array;

    try {
      ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
      ObjectOutputStream objectOut = new ObjectOutputStream(arrayOut);
      objectOut.writeObject(object);
      array = arrayOut.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Error while serializing " + object, e);
    }

    try {
      ByteArrayInputStream arrayIn = new ByteArrayInputStream(array);
      ObjectInputStream objectIn = new ObjectInputStream(arrayIn);
      return (T) objectIn.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("Error while deserializing " + object, e);
    }
  }

  /** Check that the declaration doesn't get cloned by serialization + deserialization */
  private static void checkSerialization(IPropertyRegistryKey<?> key) {
    IPropertyRegistryKey<?> serializedDeserialized = serializeAndDeserialize(key);
    assertNotSame(key, serializedDeserialized);
    assertEquals(key, serializedDeserialized);
    assertSame(key.getDeclaration(), serializedDeserialized.getDeclaration());
  }

  @Test
  void serialization() {
    checkSerialization(PropertyIds.MUTABLE);
    checkSerialization(PropertyIds.IMMUTABLE);

    checkSerialization(PropertyIds.MUTABLE_TEMPLATE);
    checkSerialization(PropertyIds.IMMUTABLE_TEMPLATE);

    checkSerialization(PropertyIds.IMMUTABLE_TEMPLATE.create("TEST"));
    checkSerialization(PropertyIds.MUTABLE_TEMPLATE.create("TEST"));
  }

  @Test
  void declaration() {
    IPropertyRegistryKeyDeclaration declaration = PropertyIds.MUTABLE.getDeclaration();
    assertThat(declaration.toString(), CoreMatchers.containsString(PropertyIds.class.getName()));

    IPropertyRegistryKeyDeclaration otherDeclaration = OtherPropertyIds.IMMUTABLE.getDeclaration();
    assertTrue(!declaration.equals(otherDeclaration));

    assertEquals(declaration, PropertyIds.IMMUTABLE.getDeclaration());
    assertEquals(declaration, PropertyIds.MUTABLE_TEMPLATE.getDeclaration());
    assertEquals(declaration, PropertyIds.IMMUTABLE_TEMPLATE.getDeclaration());
    assertEquals(ALL_KEYS, declaration.getDeclaredKeys());
  }

  @Test
  void mutableProperty() {
    assertEquals("mutable.property", PropertyIds.MUTABLE.getKey());
  }

  @Test
  void mutablePropertyTemplate() {
    assertEquals("mutable.property.template.%s", PropertyIds.MUTABLE_TEMPLATE.getFormat());

    MutablePropertyId<String> generatedProperty = PropertyIds.MUTABLE_TEMPLATE.create("TEST");
    assertEquals("mutable.property.template.TEST", generatedProperty.getKey());
    assertNull(generatedProperty.getDeclaration());
  }

  @Test
  void immutableProperty() {
    assertEquals("immutable.property", PropertyIds.IMMUTABLE.getKey());
  }

  @Test
  void immutablePropertyTemplate() {
    assertEquals("immutable.property.template.%s", PropertyIds.IMMUTABLE_TEMPLATE.getFormat());

    ImmutablePropertyId<String> generatedProperty = PropertyIds.IMMUTABLE_TEMPLATE.create("TEST");
    assertEquals("immutable.property.template.TEST", generatedProperty.getKey());
    assertNull(generatedProperty.getDeclaration());
  }
}
