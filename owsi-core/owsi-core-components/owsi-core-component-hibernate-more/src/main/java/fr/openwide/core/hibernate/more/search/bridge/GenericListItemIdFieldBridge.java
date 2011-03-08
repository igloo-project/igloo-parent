package fr.openwide.core.hibernate.more.search.bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public class GenericListItemIdFieldBridge implements FieldBridge {
	
	private static final String ID_SUFFIX = "_id";
	
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if (!(value instanceof GenericListItem)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericListItem properties.");
		}
		GenericListItem<?> listitem = (GenericListItem<?>) value;
		
		luceneOptions.addFieldToDocument(getFieldName(name), listitem.getId().toString(), document);
	}
	
	public static String getFieldName(String name) {
		return name + ID_SUFFIX;
	}

}
