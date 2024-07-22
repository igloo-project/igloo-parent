package org.iglooproject.jpa.business.generic.util;

import java.io.Serializable;
import java.util.Comparator;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * This is a default comparator, it should probably not be used in production.
 *
 * <p>Inherit from {@link AbstractGenericEntityComparator} instead.
 */
public class GenericEntityIdComparator implements Comparator<GenericEntity<Long, ?>>, Serializable {

  private static final long serialVersionUID = -9178542049081510289L;

  private static final GenericEntityIdComparator INSTANCE = new GenericEntityIdComparator();

  public static final GenericEntityIdComparator get() {
    return INSTANCE;
  }

  @Override
  public int compare(GenericEntity<Long, ?> o1, GenericEntity<Long, ?> o2) {
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

  private GenericEntityIdComparator() {}
}
