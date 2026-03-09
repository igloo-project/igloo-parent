package org.igloo.storage.impl;

import java.nio.file.Path;

public record StorageEvent(Long id, StorageEventType type, Path path) {

  public Long getId() {
    return id;
  }

  public StorageEventType getType() {
    return type;
  }

  public Path getPath() {
    return path;
  }
}
