package org.iglooproject.jpa.business.generic.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.io.Serializable;

/**
 * This class allows us to manually set the id.
 *
 * @see <a href="#PredefinedIdSequenceGenerator">PredefinedIdSequenceGenerator</a>
 */
@MappedSuperclass
public abstract class PredefinedIdGenericEntity<
        K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>>
    extends GenericEntity<K, E> implements IPredefinedIdEntity<K> {

  private static final long serialVersionUID = -3040517787818182582L;

  @Transient private K predefinedId;

  @Override
  public K getPredefinedId() {
    return predefinedId;
  }

  public void setPredefinedId(K oldApplicationId) {
    this.predefinedId = oldApplicationId;
  }
}
