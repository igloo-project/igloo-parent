package org.igloo.storage.integration.wicket;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import org.apache.wicket.request.cycle.RequestCycleListenerCollection;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.integration.wicket.exception.AbstractWicketStorageException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFichierInvalidatedException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFichierMissingException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFichierUnavailableException;
import org.igloo.storage.integration.wicket.exception.WicketStorageFileNotFoundException;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.FichierStatus;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

/**
 * Storage Fichier web resource. Delivered file is setup by {@link #getFichier(PageParameters)}.
 * Fichier status (INVALIDATED, UNAVAILABLE) is checked before delivery. Custom exceptions are used
 * and may be handled by {@link RequestCycleListenerCollection} as needed.
 *
 * @see AbstractWicketStorageException
 */
public abstract class AbstractFichierFileStorageWebResource
    extends AbstractFichierStoreWebResource {

  @SpringBean private IStorageService storageService;

  private static final long serialVersionUID = 4055986406247245615L;

  protected AbstractFichierFileStorageWebResource() {
    super();
  }

  protected AbstractFichierFileStorageWebResource(
      SerializableConsumer<ResourceResponse> resourceResponseConsumer) {
    super(resourceResponseConsumer);
  }

  public abstract Fichier getFichier(PageParameters parameters)
      throws ServiceException, SecurityServiceException;

  @Override
  protected FichierStoreResourceStream getFileStoreResourceStream(PageParameters parameters)
      throws ServiceException, SecurityServiceException {
    Fichier fichier = getFichier(parameters);

    // handle special cases
    if (fichier == null) {
      throw new WicketStorageFichierMissingException(
          "Fichier entity %s is missing.".formatted(fichier));
    } else if (FichierStatus.UNAVAILABLE.equals(fichier.getStatus())) {
      throw new WicketStorageFichierUnavailableException(
          "Fichier %s is unavailable.".formatted(fichier));
    } else if (FichierStatus.INVALIDATED.equals(fichier.getStatus())) {
      throw new WicketStorageFichierInvalidatedException(
          "Fichier %s is unavailable.".formatted(fichier));
    }
    try {
      File file = storageService.getFile(fichier);
      return new FichierStoreResourceStream(
          file, Optional.ofNullable(fichier.getFilename()).orElse(""));
    } catch (FileNotFoundException e) {
      throw new WicketStorageFileNotFoundException(e.getMessage());
    }
  }
}
