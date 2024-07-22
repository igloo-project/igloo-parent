package basicapp.back.config.hibernate.type;

import basicapp.back.business.common.model.PhoneNumber;
import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class PhoneNumberType extends AbstractImmutableMaterializedStringValueUserType<PhoneNumber> {

  @Override
  public Class<PhoneNumber> returnedClass() {
    return PhoneNumber.class;
  }

  @Override
  protected PhoneNumber instantiate(String value) {
    return PhoneNumber.buildClean(value);
  }
}
