package test.jpa.more.business.history.model.bean;

import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import test.jpa.more.business.entity.model.TestEntity;

public final class TestHistoryLogAdditionalInformationBean
    extends AbstractHistoryLogAdditionalInformationBean {

  public static final TestHistoryLogAdditionalInformationBean empty() {
    return new TestHistoryLogAdditionalInformationBean();
  }

  public static final TestHistoryLogAdditionalInformationBean of(TestEntity secondaryObject) {
    return new TestHistoryLogAdditionalInformationBean(secondaryObject);
  }

  private TestHistoryLogAdditionalInformationBean(Object... secondaryObjects) {
    super(secondaryObjects);
  }
}
