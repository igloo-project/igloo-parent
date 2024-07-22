package test;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.igloo.storage.model.StorageFailure;

public class StorageSoftAssertions extends AutoCloseableSoftAssertions {
  public StorageFailureAssert assertThat(StorageFailure failure) {
    return proxy(StorageFailureAssert.class, StorageFailure.class, failure);
  }
}
