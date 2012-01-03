package fr.openwide.core.hibernate.more.business.generic.util;

import java.io.Serializable;
import java.util.Comparator;

import org.hibernate.Hibernate;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public class GenericListItemComparator implements Comparator<GenericListItem<?>>, Serializable {
	
	private static final long serialVersionUID = 2641214465049416075L;

	public static final Comparator<GenericListItem<?>> INSTANCE = new GenericListItemComparator();
	
	@Override
	public int compare(GenericListItem<?> o1, GenericListItem<?> o2) {
		int order = 0;
		
		if (order == 0 && !Hibernate.getClass(o1).equals(Hibernate.getClass(o2))) {
			order = GenericEntity.DEFAULT_STRING_COLLATOR.compare(Hibernate.getClass(o1).getSimpleName(),
					Hibernate.getClass(o2).getSimpleName());
		}
		
		// on trie en priorité sur la position, puis sur le libellé et enfin par l'identifiant
		if (order == 0 && o1.getPosition() != null && o2.getPosition() != null) {
			order = o1.getPosition().compareTo(o2.getPosition());
		}
		
		if (order == 0 && o1.getPosition() != null && o2.getPosition() == null) {
			order = -1;
		}
		
		if (order == 0 && o1.getPosition() == null && o2.getPosition() != null) {
			order = 1;
		}
		
		if (order == 0 && o1.getLabel() != null) {
			order = GenericEntity.DEFAULT_STRING_COLLATOR.compare(o1.getLabel(), o2.getLabel());
		}
		
		if (order == 0 && o1.getId() != null && o2.getId() != null) {
			order = o1.getId().compareTo(o2.getId());
		}
		
		if (order == 0 && o1.getId() != null && o2.getId() == null) {
			order = -1;
		}
		
		if (order == 0 && o1.getId() == null && o2.getId() != null) {
			order = 1;
		}
		return order;
	}

}
