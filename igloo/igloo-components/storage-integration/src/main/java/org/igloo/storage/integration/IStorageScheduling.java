package org.igloo.storage.integration;

import org.springframework.context.annotation.Configuration;

/**
 * Annotate a bean (like a @{@link Configuration} bean) with this marker interface if you want to
 * override default scheduling configuration.
 */
public interface IStorageScheduling {

  void cleaning();

  void housekeeping();
}
