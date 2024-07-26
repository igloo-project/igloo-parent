package org.iglooproject.basicapp.core.config.hibernate.type;

import org.iglooproject.basicapp.core.business.common.model.EmailAddress;
import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class EmailAddressType
    extends AbstractImmutableMaterializedStringValueUserType<EmailAddress> {

  @Override
  public Class<EmailAddress> returnedClass() {
    return EmailAddress.class;
  }

  @Override
  protected EmailAddress instantiate(String value) {
    return new EmailAddress(value);
  }
}
