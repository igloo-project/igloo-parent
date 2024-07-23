package org.iglooproject.jpa.batch.monitor;

import java.util.concurrent.atomic.AtomicInteger;

public class ProcessorMonitorContext {

  private static final ThreadLocal<ProcessorMonitorContext> THREAD_LOCAL = new ThreadLocal<>();

  private final AtomicInteger totalItems = new AtomicInteger(0);

  private final AtomicInteger doneItems = new AtomicInteger(0);

  private final AtomicInteger failedItems = new AtomicInteger(0);

  private final AtomicInteger ignoredItems = new AtomicInteger(0);

  public static void unset() {
    THREAD_LOCAL.remove();
  }

  public static ProcessorMonitorContext get() {
    ProcessorMonitorContext context = THREAD_LOCAL.get();
    if (context == null) {
      throw new IllegalStateException(
          "Le ThreadLocal MonitorContext doit être mis en place avant toute utilisation");
    }
    return context;
  }

  public static ThreadLocal<ProcessorMonitorContext> getThreadLocal() {
    return THREAD_LOCAL;
  }

  public static void set(ProcessorMonitorContext context) {
    THREAD_LOCAL.set(context);
  }

  public AtomicInteger getTotalItems() {
    return totalItems;
  }

  public AtomicInteger getDoneItems() {
    return doneItems;
  }

  public AtomicInteger getFailedItems() {
    return failedItems;
  }

  public AtomicInteger getIgnoredItems() {
    return ignoredItems;
  }
}
