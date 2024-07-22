package igloo.jpa.batch.runnable;

public abstract class ReadWriteBatchRunnable<E> extends AbstractBatchRunnable<E> {

  @Override
  public final Writeability getWriteability() {
    return Writeability.READ_WRITE;
  }
}
