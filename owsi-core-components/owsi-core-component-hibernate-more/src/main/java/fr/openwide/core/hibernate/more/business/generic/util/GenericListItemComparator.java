package fr.openwide.core.hibernate.more.business.generic.util;

import java.io.Serializable;
import java.util.Comparator;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public class GenericListItemComparator implements Comparator<GenericListItem<?>>, Serializable {
	
	private static final long serialVersionUID = 2641214465049416075L;

	public static final Comparator<GenericListItem<?>> INSTANCE = new GenericListItemComparator();
	
	@Override
	public int compare(GenericListItem<?> o1, GenericListItem<?> o2) {
		if (!o1.getClass().equals(o2.getClass())) {
			throw new IllegalArgumentException("Compared objects must have the same type.");
		}
		
		// on trie en priorité sur la position, puis sur le libellé et enfin par l'identifiant
		int order = 0;
		if (o1.getPosition() != null) {
			order = o1.getPosition().compareTo(o2.getPosition());
		}
		if (order == 0 && o1.getLabel() != null) {
			order = GenericEntity.DEFAULT_STRING_COLLATOR.compare(o1.getLabel(), o2.getLabel());
		}
		if (order == 0) {
			order = o1.getId().compareTo(o2.getId());
		}
		return order;
	}

}
