package test;

import org.assertj.core.api.Assertions;
import org.igloo.storage.model.StorageFailure;

public class StorageAssertions extends Assertions {

	public static StorageFailureAssert assertThat(StorageFailure actual) {
		return new StorageFailureAssert(actual);
	}

}
