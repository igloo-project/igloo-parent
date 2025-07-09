package org.iglooproject.jpa.more.business.history.model.embeddable;

import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

public interface IHistoryValueProvider<H extends IHistoryValue> {
  H build(String label, String serialized, GenericEntityReference<Long, ?> reference);
}
