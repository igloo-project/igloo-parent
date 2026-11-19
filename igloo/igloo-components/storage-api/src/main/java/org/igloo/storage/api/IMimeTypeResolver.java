package org.igloo.storage.api;

import org.jspecify.annotations.Nullable;

/**
 * {@link IMimeTypeResolver} allow to select a mimetype from a filename. Returned mimetype cannot be
 * null. If mimetype cannot be resolved, <code>application/octet-stream</code> is used.
 */
public interface IMimeTypeResolver {

  /**
   * @see IMimeTypeResolver
   */
  String resolve(@Nullable String filename);
}
