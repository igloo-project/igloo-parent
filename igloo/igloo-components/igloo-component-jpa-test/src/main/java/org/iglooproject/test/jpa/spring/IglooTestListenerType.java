package org.iglooproject.test.jpa.spring;

/**
 * Control which events are handled by a {@link IglooTestExecutionListener} instance. Other events
 * are ignored. Special value {@link #ANY} can be used to let {@link IIglooTestListener} receives
 * all events.
 */
public enum IglooTestListenerType {
  CLASS,
  METHOD,
  EXECUTION,
  /** Trigger {@link #CLASS} or {@link #METHOD} based on test instance lifecycle. */
  AUTO,
  /** Trigger all (class, method, execution) events */
  ANY;

  boolean match(IglooTestListenerType type) {
    return type == this || this == ANY;
  }
}
