package test;

import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractLocalDateTimeAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ObjectAssert;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;

public class StorageFailureAssert extends AbstractAssert<StorageFailureAssert, StorageFailure> {

  protected StorageFailureAssert(StorageFailure actual) {
    super(actual, StorageFailureAssert.class);
  }

  public StorageFailureAssert lastFailureOn(Consumer<AbstractLocalDateTimeAssert<?>> consumer) {
    consumer.accept(
        extracting(StorageFailure::getLastFailureOn, InstanceOfAssertFactories.LOCAL_DATE_TIME)
            .as("lastFailureOn"));
    return this;
  }

  public StorageFailureAssert creationTime(Consumer<AbstractLocalDateTimeAssert<?>> consumer) {
    consumer.accept(
        extracting(StorageFailure::getCreationTime, InstanceOfAssertFactories.LOCAL_DATE_TIME)
            .as("creationTime"));
    return this;
  }

  public StorageFailureAssert acknowledgedOn(Consumer<AbstractLocalDateTimeAssert<?>> consumer) {
    consumer.accept(
        extracting(StorageFailure::getAcknowledgedOn, InstanceOfAssertFactories.LOCAL_DATE_TIME)
            .as("acknowledgedOn"));
    return this;
  }

  public StorageFailureAssert fixedOn(Consumer<AbstractLocalDateTimeAssert<?>> consumer) {
    consumer.accept(
        extracting(StorageFailure::getFixedOn, InstanceOfAssertFactories.LOCAL_DATE_TIME)
            .as("fixedOn"));
    return this;
  }

  public StorageFailureAssert status(Consumer<ObjectAssert<StorageFailureStatus>> consumer) {
    consumer.accept(
        extracting(
                StorageFailure::getStatus,
                InstanceOfAssertFactories.type(StorageFailureStatus.class))
            .as("status"));
    return this;
  }

  public StorageFailureAssert type(Consumer<ObjectAssert<StorageFailureType>> consumer) {
    consumer.accept(
        extracting(
                StorageFailure::getType, InstanceOfAssertFactories.type(StorageFailureType.class))
            .as("type"));
    return this;
  }

  public StorageFailureAssert fichier(Consumer<ObjectAssert<Fichier>> consumer) {
    consumer.accept(
        extracting(StorageFailure::getFichier, InstanceOfAssertFactories.type(Fichier.class))
            .as("fichier"));
    return this;
  }

  public StorageFailureAssert consistencyCheck(
      Consumer<ObjectAssert<StorageConsistencyCheck>> consumer) {
    consumer.accept(
        extracting(
                StorageFailure::getConsistencyCheck,
                InstanceOfAssertFactories.type(StorageConsistencyCheck.class))
            .as("consistencyCheck"));
    return this;
  }

  public StorageFailureAssert path(Consumer<AbstractStringAssert<?>> consumer) {
    consumer.accept(
        extracting(StorageFailure::getPath, InstanceOfAssertFactories.STRING).as("path"));
    return this;
  }

  public StorageFailureAssert id(Consumer<AbstractLongAssert<?>> consumer) {
    consumer.accept(extracting(StorageFailure::getId, InstanceOfAssertFactories.LONG).as("path"));
    return this;
  }
}
