package org.igloo.storage.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link IStorageService} is the main interface to use storage module. It allows to create new
 * {@link StorageUnit}, to add, validate, invalidate, remove and get {@link Fichier} and associated
 * data.
 */
@Transactional
public interface IStorageService {

  /** Creation of {@link StorageUnit} from type into storage */
  StorageUnit createStorageUnit(IStorageUnitType type, StorageUnitCheckType checkType);

  /**
   * Creation of {@link StorageUnit} with an explicit path. If path is null, automatic path
   * generation is used (based on storage.path property and unit name).
   */
  StorageUnit createStorageUnit(
      IStorageUnitType type, StorageUnitCheckType checkType, @Nullable String path);

  /**
   * @see IStorageService#addFichier(String, IFichierType, InputStream, GenericEntity)
   *     <p>Fichier is associated with a null author.
   */
  Fichier addFichier(String filename, IFichierType fichierType, InputStream inputStream);

  /**
   * Creation of {@link Fichier} with status {@link FichierStatus#TRANSIENT} and storage of
   * associated file from inputStream into storage. If it is not validated, it will at last be
   * deleted along with associated file.
   *
   * @see IStorageService#addFichier(String, IFichierType, InputStream, GenericEntity)
   */
  Fichier addFichier(
      String filename,
      IFichierType fichierType,
      InputStream inputStream,
      @Nullable GenericEntity<Long, ?> author);

  /**
   * The {@link Fichier} is marked {@link FichierStatus#ALIVE} and {@link Fichier#validationDate} is
   * initialized.
   *
   * <p>If the {@link Fichier} is detached it will be reloaded (with a logger warn).
   */
  void validateFichier(Fichier fichierDetached);

  /**
   * The {@link Fichier} is marked {@link FichierStatus#INVALIDATED} and {@link
   * Fichier#invalidationDate} is initialized. The entity will soon be deleted along with associated
   * file.
   *
   * <p>If the {@link Fichier} is detached it will be reloaded (with a logger warn).
   */
  void invalidateFichier(Fichier fichierDetached);

  /** Deletion of {@link Fichier} and associated file */
  void removeFichier(Fichier fichier);

  /** Update {@code filename} of {@link Fichier}. */
  void updateFichierFilename(Fichier fichier, String filename);

  @Transactional(readOnly = true)
  @Nullable Fichier getFichierById(Long id);

  /**
   * Get file associated to {@link Fichier}. If File cannot be found on filesystem, or if Fichier
   * status is {@link FichierStatus#INVALIDATED} or {@link FichierStatus#UNAVAILABLE}, this method
   * throws a {@link FileNotFoundException}. See {@link #getFile(Fichier, boolean, boolean)} to
   * disable these checks.
   *
   * @throws FileNotFoundException if file cannot be found, is not readable or {@link
   *     Fichier#getStatus()} is {@link FichierStatus#INVALIDATED} or {@link
   *     FichierStatus#UNAVAILABLE}.
   */
  @Transactional(readOnly = true)
  File getFile(Fichier fichier) throws FileNotFoundException;

  @Transactional(readOnly = true)
  File getFile(Fichier fichier, boolean checkInvalidatedOrUnavailable, boolean checkExists)
      throws FileNotFoundException;

  /**
   * Perform a consistency check on a {@link StorageUnit}. Check can be performed with or without
   * checksum validation.
   */
  List<StorageConsistencyCheck> checkConsistency(StorageUnit unit, boolean checksumValidation);

  /**
   * Copy an existing {@link StorageUnitStatus#ALIVE} unit, switch it to {@link
   * StorageUnitStatus#ARCHIVED}, and return new {@link StorageUnit}. <code>original</code> can be a
   * detached instance. It will be reloaded.
   *
   * @throws IllegalStateException if original is not {@link StorageUnitStatus#ALIVE}
   */
  StorageUnit splitStorageUnit(StorageUnit original);

  /**
   * Check creator of {@link Fichier}. Check if {@link Fichier#getCreatedBy()} has same class type
   * than <code>clazz</code> and same id than <code>id</code>
   */
  @Transactional(readOnly = true)
  public boolean isCreatedBy(Fichier fichier, Class<?> clazz, @Nullable Long id);
}
