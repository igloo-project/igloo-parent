package org.igloo.storage.impl;

import java.nio.file.Path;
import java.util.List;

public interface IStorageTransactionResourceManager {

  void addEvent(Long id, StorageEventType type, Path path);

  List<StorageEvent> getEvents();

  void unbind();
}
