package org.igloo.storage.model.atomic;

import org.igloo.storage.model.StorageUnit;

public enum StorageUnitStatus {

  /** {@link StorageUnit} is in use and can receive new file */
  ALIVE,

  /** {@link StorageUnit} does not store new file */
  ARCHIVED;
}
