package org.igloo.storage.model.atomic;

import org.igloo.storage.model.Fichier;

public enum FichierStatus {

  /**
   * The {@link Fichier} is created but not yet marked alived. If it is not moved to {@link #ALIVE}
   * status, it will at last be deleted (configurable delay).
   */
  TRANSIENT,

  /** The {@link Fichier} is in use. */
  ALIVE,

  /**
   * The deletion of {@link Fichier} has been asked. The entity and the associated file will soon be
   * deleted (configurable delay).
   */
  INVALIDATED;
}
