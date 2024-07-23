package org.iglooproject.test.jpa.batch;

class TestBatchException1 extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public TestBatchException1() {
    super();
  }

  public TestBatchException1(String message, Throwable cause) {
    super(message, cause);
  }

  public TestBatchException1(String message) {
    super(message);
  }

  public TestBatchException1(Throwable cause) {
    super(cause);
  }
}
