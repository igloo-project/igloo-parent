package fr.openwide.core.hibernate.business.generic.util;

import java.io.Serializable;
import java.util.Comparator;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

public class GenericEntityIdComparator implements Comparator<GenericEntity<Integer, ?>>, Serializable {
	
	private static final long serialVersionUID = -9178542049081510289L;

	public static final Comparator<GenericEntity<Integer, ?>> INSTANCE = new GenericEntityIdComparator();

	@Override
	public int compare(GenericEntity<Integer, ?> o1, GenericEntity<Integer, ?> o2) {
		if (o1.getId() != null && o2.getId() != null) {
			return o1.getId().compareTo(o2.getId());
		} else if (o1.getId() != null && o2.getId() == null) {
			// les éléments les plus récents sont les plus élevés pour le comparateur
			return -1;
		} else if (o1.getId() == null && o2.getId() != null) {
			return 1;
		}
		return 0;
	}

}
