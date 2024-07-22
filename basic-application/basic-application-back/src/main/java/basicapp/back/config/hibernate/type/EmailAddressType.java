package basicapp.back.config.hibernate.type;

import basicapp.back.business.common.model.EmailAddress;
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
