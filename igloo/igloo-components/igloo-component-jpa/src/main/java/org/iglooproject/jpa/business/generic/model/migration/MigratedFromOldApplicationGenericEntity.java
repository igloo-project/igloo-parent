package org.iglooproject.jpa.business.generic.model.migration;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.iglooproject.jpa.business.generic.model.PredefinedIdGenericEntity;

/**
 * This class should only be used as a temporary measure to migrate entities from an old application
 * to a new one.
 *
 * <p>It allows us to keep the old id as the new id.
 *
 * @see MigratedFromOldApplicationSequenceGenerator
 */
@MappedSuperclass
public abstract class MigratedFromOldApplicationGenericEntity<
        K extends Serializable & Comparable<K>, E extends PredefinedIdGenericEntity<K, E>>
    extends PredefinedIdGenericEntity<K, E> implements IMigratedFromOldApplicationEntity<K> {

  private static final long serialVersionUID = 2034570162020079499L;

  @Column(nullable = false)
  private boolean migrated = false;

  @Override
  public boolean isMigrated() {
    return migrated;
  }

  @Override
  public void setMigrated(boolean migrated) {
    this.migrated = migrated;
  }
}
