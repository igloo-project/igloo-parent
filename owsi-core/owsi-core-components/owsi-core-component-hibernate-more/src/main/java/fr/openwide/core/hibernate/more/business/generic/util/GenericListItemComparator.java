package fr.openwide.core.hibernate.more.business.generic.util;

import java.util.Comparator;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public class GenericListItemComparator<E extends GenericListItem<E>> implements Comparator<E> {

	@Override
	public int compare(E o1, E o2) {
		// on trie en priorité sur la position, puis sur le libellé et enfin par l'identifiant
		int order = 0;
		if (o1.getPosition() != null) {
			order = o1.getPosition().compareTo(o2.getPosition());
		}
		if (order == 0 && o1.getLabel() != null) {
			order = GenericEntity.DEFAULT_STRING_COLLATOR.compare(o1.getLabel(), o2.getLabel());
		}
		if (order == 0) {
			order = o1.compareTo(o2);
		}
		return order;
	}

}
