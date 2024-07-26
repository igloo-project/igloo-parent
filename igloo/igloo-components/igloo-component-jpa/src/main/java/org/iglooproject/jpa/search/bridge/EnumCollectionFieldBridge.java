package org.iglooproject.jpa.search.bridge;

import java.util.Collection;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

public class EnumCollectionFieldBridge implements FieldBridge, StringBridge {

  @Override
  public String objectToString(Object object) {
    if (object == null) {
      return null;
    }
    if (!(object instanceof Enum<?>)) {
      throw new IllegalArgumentException("This FieldBridge only supports Enum properties.");
    }
    Enum<?> enumValue = (Enum<?>) object;
    return enumValue.name();
  }

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value == null) {
      return;
    }
    if (!(value instanceof Collection)) {
      throw new IllegalArgumentException(
          "This FieldBridge only supports Collection of Enum properties.");
    }
    Collection<?> objects = (Collection<?>) value;

    for (Object object : objects) {
      luceneOptions.addFieldToDocument(name, objectToString(object), document);
    }
  }
}
