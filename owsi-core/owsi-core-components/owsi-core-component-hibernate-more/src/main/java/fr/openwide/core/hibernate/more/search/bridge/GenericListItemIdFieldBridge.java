package fr.openwide.core.hibernate.more.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public class GenericListItemIdFieldBridge implements FieldBridge, StringBridge {
	
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if (!(value instanceof GenericListItem)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericListItem properties.");
		}
		GenericListItem<?> listItem = (GenericListItem<?>) value;
		
		luceneOptions.addFieldToDocument(name, listItem.getId().toString(), document);
	}
	
	@Override
	public String objectToString(Object object) {
		if (!(object instanceof GenericListItem)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericListItem properties.");
		}
		GenericListItem<?> listItem = (GenericListItem<?>) object;
		return listItem.getId().toString();
	}
	
}