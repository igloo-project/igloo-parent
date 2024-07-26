package org.iglooproject.jpa.batch.runnable;

public abstract class ReadOnlyBatchRunnable<E> extends AbstractBatchRunnable<E> {

  @Override
  public final Writeability getWriteability() {
    return Writeability.READ_ONLY;
  }
}
