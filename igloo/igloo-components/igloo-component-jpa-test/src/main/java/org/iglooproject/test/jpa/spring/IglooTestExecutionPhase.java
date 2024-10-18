package org.iglooproject.test.jpa.spring;

public enum IglooTestExecutionPhase {
  PREPARE_INSTANCE,
  BEFORE_CLASS,
  BEFORE_METHOD,
  BEFORE_EXECUTION,
  AFTER_EXECUTION,
  AFTER_METHOD,
  AFTER_CLASS;
}