package org.iglooproject.jpa.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

public class GenericEntityReferenceFieldBridge implements FieldBridge, StringBridge {

  @Override
  public String objectToString(Object object) {
    if (object == null) {
      return null;
    }
    if (!(object instanceof GenericEntityReference)) {
      throw new IllegalArgumentException(
          "This FieldBridge only supports GenericEntityReference properties.");
    }
    GenericEntityReference<?, ?> entityReference = (GenericEntityReference<?, ?>) object;
    Object id = entityReference.getId();
    Class<?> clazz = entityReference.getType();
    if (clazz == null) {
      return null;
    } else {
      return clazz.getCanonicalName() + "|" + id.toString();
    }
  }

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value == null) {
      return;
    }
    luceneOptions.addFieldToDocument(name, objectToString(value), document);
  }
}
