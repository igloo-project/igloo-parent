package org.iglooproject.jpa.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;

public class NullEncodingGenericEntityReferenceFieldBridge
    extends GenericEntityReferenceFieldBridge {

  public static final String NULL_TOKEN = "_NULL_";

  @Override
  public String objectToString(Object object) {
    String result = super.objectToString(object);
    if (result == null) {
      return NULL_TOKEN;
    }
    return result;
  }

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    luceneOptions.addFieldToDocument(name, objectToString(value), document);
  }
}
