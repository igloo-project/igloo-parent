package org.igloo.storage.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.igloo.storage.api.IMimeTypeResolver;
import org.iglooproject.commons.util.mime.IMediaType;
import org.iglooproject.commons.util.mime.IMediaTypeRegistry;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.commons.util.mime.MediaTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MimeTypeResolver implements IMimeTypeResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(MimeTypeResolver.class);

  private final IMediaTypeRegistry mediaTypeRegistry;

  public MimeTypeResolver() {
    this(MediaTypeRegistry.of(MediaType.class));
  }

  public MimeTypeResolver(IMediaTypeRegistry mediaTypeRegistry) {
    this.mediaTypeRegistry = mediaTypeRegistry;
  }

  @Override
  public @Nonnull String resolve(@Nullable String filename) {
    if (filename == null) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No mimetype found, called with a null filename");
      }
      return MediaType.APPLICATION_OCTET_STREAM.mime();
    } else if (!filename.contains(".")) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No mimetype found, no extension on {}", filename);
      }
      return MediaType.APPLICATION_OCTET_STREAM.mime();
    } else {
      return resolve(filename, filename.split("\\."));
    }
  }

  private String resolve(@Nonnull String filename, @Nonnull String[] parts) {
    if (parts.length == 1) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No mimetype found for {} (blank extension)", filename);
      }
      return MediaType.APPLICATION_OCTET_STREAM.mime();
    }
    String extension = parts[parts.length - 1];
    if (extension.isBlank()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No mimetype found for {} (blank extension)", filename);
      }
      return MediaType.APPLICATION_OCTET_STREAM.mime();
    } else {
      return resolve(filename, extension);
    }
  }

  private String resolve(@Nonnull String filename, @Nonnull String extension) {
    IMediaType mediaType = mediaTypeRegistry.fromExtension(extension);
    if (mediaType != null) {
      return mediaType.mime();
    }
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("No mimetype found for {} (extension: {})", filename, extension);
    }
    return MediaType.APPLICATION_OCTET_STREAM.mime();
  }
}
