package fr.openwide.core.hibernate.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

public class GenericEntityIdFieldBridge implements FieldBridge, StringBridge {
	
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if (!(value instanceof GenericEntity)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericEntity properties.");
		}
		GenericEntity<?, ?> entity = (GenericEntity<?, ?>) value;
		
		luceneOptions.addFieldToDocument(name, entity.getId().toString(), document);
	}
	
	@Override
	public String objectToString(Object object) {
		if (!(object instanceof GenericEntity)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericEntity properties.");
		}
		GenericEntity<?, ?> entity = (GenericEntity<?, ?>) object;
		return entity.getId().toString();
	}
	
}