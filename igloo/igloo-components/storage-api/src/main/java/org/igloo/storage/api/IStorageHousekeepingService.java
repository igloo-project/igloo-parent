package org.igloo.storage.api;

import org.igloo.storage.model.Fichier;

public interface IStorageHousekeepingService {

  /**
   * Perform housekeeping job:
   *
   * <ul>
   *   <li>Check storage units consistency (each storage unit can disable or delay consistency check
   *       as needed).
   *   <li>Perform overflowing storage unit creation (each storage unit can disable automatic
   *       switch).
   * </ul>
   */
  void housekeeping(boolean consistencyDisabled, boolean splitStorageUnitDisabled);

  /**
   * Clean invalidated and transient {@link Fichier}. All invalidated {@link Fichier} are eligible
   * for removal. Transient {@link Fichier} are removed after a grace period.
   *
   * @param cleaningInvalidatedDisabled skip cleaning of invalidated files
   * @param cleaningTransientDisabled skip cleaning of transient files
   */
  void cleaning(boolean cleaningInvalidatedDisabled, boolean cleaningTransientDisabled);
}
