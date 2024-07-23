package org.iglooproject.jpa.more.util.transaction.exception;

import org.springframework.core.NestedRuntimeException;

public class TransactionSynchronizationException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  public TransactionSynchronizationException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public TransactionSynchronizationException(String msg) {
    super(msg);
  }
}
