package org.iglooproject.jpa.search.bridge;

import java.util.Collection;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

public class StringCollectionFieldBridge implements FieldBridge, StringBridge {

  @Override
  public String objectToString(Object object) {
    if (object == null) {
      return null;
    }
    if (!(object instanceof String)) {
      throw new IllegalArgumentException("This FieldBridge only supports String properties.");
    }
    return (String) object;
  }

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value == null) {
      return;
    }
    if (!(value instanceof Collection)) {
      throw new IllegalArgumentException(
          "This FieldBridge only supports Collection of String properties.");
    }
    Collection<?> objects = (Collection<?>) value;

    for (Object object : objects) {
      luceneOptions.addFieldToDocument(name, objectToString(object), document);
    }
  }
}
