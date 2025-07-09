package test.business.model;

import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;

public final class HistoryLogAdditionalInformationBean
    extends AbstractHistoryLogAdditionalInformationBean {

  public static final HistoryLogAdditionalInformationBean empty() {
    return new HistoryLogAdditionalInformationBean();
  }

  public HistoryLogAdditionalInformationBean(Object... secondaryObjects) {
    super(secondaryObjects);
  }
}
