package org.iglooproject.test.jpa.batch;

class TestBatchException2 extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public TestBatchException2() {
    super();
  }

  public TestBatchException2(String message, Throwable cause) {
    super(message, cause);
  }

  public TestBatchException2(String message) {
    super(message);
  }

  public TestBatchException2(Throwable cause) {
    super(cause);
  }
}
