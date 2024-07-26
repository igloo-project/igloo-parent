package org.iglooproject.jpa.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;

public class NullEncodingGenericEntityIdFieldBridge extends AbstractGenericEntityIdFieldBridge {
  private static final String NULL_TOKEN = "_NULL_";

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    luceneOptions.addFieldToDocument(name, objectToString(value), document);
  }

  @Override
  public String objectToString(Object object) {
    if (object == null) {
      return NULL_TOKEN;
    }

    return super.objectToString(object);
  }
}
