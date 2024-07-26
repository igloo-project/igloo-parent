package org.iglooproject.jpa.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

public class MaterializedStringValueFieldBridge implements FieldBridge, StringBridge {

  @Override
  public String objectToString(Object object) {
    if (object == null) {
      return null;
    }
    if (!(object instanceof AbstractMaterializedPrimitiveValue)) {
      throw new IllegalArgumentException(
          "This FieldBridge only supports AbstractMaterializedPrimitiveValue<String, ?> properties.");
    }
    AbstractMaterializedPrimitiveValue<?, ?> materializedStringValue =
        (AbstractMaterializedPrimitiveValue<?, ?>) object;
    Object value = materializedStringValue.getValue();
    if (!(value instanceof String)) {
      throw new IllegalArgumentException(
          "This FieldBridge only supports AbstractMaterializedPrimitiveValue<String, ?> properties.");
    }
    return (String) value;
  }

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    luceneOptions.addFieldToDocument(name, objectToString(value), document);
  }
}
