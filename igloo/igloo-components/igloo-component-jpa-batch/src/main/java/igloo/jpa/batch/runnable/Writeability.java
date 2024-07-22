package igloo.jpa.batch.runnable;

/**
 * Whether a given {@link IBatchRunnable} is expected to run with read-only access to the database
 * or with read-write access.
 */
public enum Writeability {
  READ_ONLY,
  READ_WRITE;
}
