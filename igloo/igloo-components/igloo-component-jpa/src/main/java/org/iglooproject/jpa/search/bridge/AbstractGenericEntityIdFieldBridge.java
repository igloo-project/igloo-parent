package org.iglooproject.jpa.search.bridge;

import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.StringBridge;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public abstract class AbstractGenericEntityIdFieldBridge implements FieldBridge, StringBridge {

  @Override
  public String objectToString(Object object) {
    if (object == null) {
      return null;
    }
    if (!(object instanceof GenericEntity)) {
      throw new IllegalArgumentException(
          "This FieldBridge only supports GenericEntity properties.");
    }
    GenericEntity<?, ?> entity = (GenericEntity<?, ?>) object;
    Object id = entity.getId();
    // The ID may be null if the FieldBridge is being used while building a query.
    return id == null ? "" : id.toString();
  }
}
