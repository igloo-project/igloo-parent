package org.iglooproject.jpa.batch.monitor;

import java.util.concurrent.Callable;

public class ThreadLocalInitializingCallable<T, C> implements Callable<T> {

  public final Callable<T> callable;

  public final ThreadLocal<C> threadLocalContext;

  public final C context;

  public ThreadLocalInitializingCallable(
      Callable<T> callable, ThreadLocal<C> threadLocalContext, C context) {
    this.callable = callable;
    this.threadLocalContext = threadLocalContext;
    this.context = context;
  }

  @Override
  public T call() throws Exception {
    threadLocalContext.set(this.context);
    try {
      return callable.call();
    } finally {
      threadLocalContext.remove();
    }
  }
}
