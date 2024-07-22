package org.iglooproject.wicket.more.markup.head;

import org.apache.wicket.markup.head.IReferenceHeaderItem;
import org.apache.wicket.markup.head.ResourceAggregator.RecordedHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;

public interface IHeaderItemSpecificOrder {

  Integer order(RecordedHeaderItem recordedeaderItem);

  default ResourceReference getResourceReference(RecordedHeaderItem recordedeaderItem) {
    if (recordedeaderItem.getItem() instanceof IReferenceHeaderItem) {
      return ((IReferenceHeaderItem) recordedeaderItem.getItem()).getReference();
    }
    return null;
  }
}
