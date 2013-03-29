package fr.openwide.core.jpa.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

import fr.openwide.core.jpa.hibernate.usertype.AbstractMaterializedStringValue;

public class MaterializedStringValueFieldBridge implements FieldBridge, StringBridge {

	@Override
	public String objectToString(Object object) {
		if (object == null) {
			return null;
		}
		if (!(object instanceof AbstractMaterializedStringValue)) {
			throw new IllegalArgumentException("This FieldBridge only supports AbstractMaterializedStringValue properties.");
		}
		AbstractMaterializedStringValue<?> materializedStringValue = (AbstractMaterializedStringValue<?>) object;
		return materializedStringValue.getValue();
	}

	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		luceneOptions.addFieldToDocument(name, objectToString(value), document);
	}

}
