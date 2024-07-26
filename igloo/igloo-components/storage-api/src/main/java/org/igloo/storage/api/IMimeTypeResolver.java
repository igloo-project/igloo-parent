package org.igloo.storage.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * {@link IMimeTypeResolver} allow to select a mimetype from a filename. Returned mimetype cannot be
 * null. If mimetype cannot be resolved, <code>application/octet-stream</code> is used.
 */
public interface IMimeTypeResolver {

  /**
   * @see IMimeTypeResolver
   */
  @Nonnull
  String resolve(@Nullable String filename);
}
