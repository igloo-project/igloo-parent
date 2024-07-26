package org.igloo.storage.model.atomic;

public enum StorageFailureType {
  MISSING_FILE,
  MISSING_ENTITY,
  SIZE_MISMATCH,
  CHECKSUM_MISMATCH;
}
