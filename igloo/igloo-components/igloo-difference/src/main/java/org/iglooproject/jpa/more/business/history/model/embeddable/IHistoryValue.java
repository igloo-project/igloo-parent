package org.iglooproject.jpa.more.business.history.model.embeddable;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

public interface IHistoryValue {

  String getLabel();

  String getSerialized();

  GenericEntityReference<Long, GenericEntity<Long, ?>> getReference();

  default <H extends IHistoryValue> H toValue(IHistoryValueProvider<H> supplier) {
    return supplier.build(getLabel(), getSerialized(), getReference());
  }
}
