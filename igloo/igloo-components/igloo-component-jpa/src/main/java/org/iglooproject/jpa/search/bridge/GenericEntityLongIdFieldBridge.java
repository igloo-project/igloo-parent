package org.iglooproject.jpa.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;

public class GenericEntityLongIdFieldBridge extends AbstractGenericEntityLongIdFieldBridge {

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value == null) {
      return;
    }
    luceneOptions.addNumericFieldToDocument(name, objectToLong(value), document);
  }
}
