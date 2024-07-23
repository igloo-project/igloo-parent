package org.iglooproject.basicapp.core.config.hibernate.type;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class PostalCodeType extends AbstractImmutableMaterializedStringValueUserType<PostalCode> {

  @Override
  public Class<PostalCode> returnedClass() {
    return PostalCode.class;
  }

  @Override
  protected PostalCode instantiate(String value) {
    return new PostalCode(value);
  }
}
