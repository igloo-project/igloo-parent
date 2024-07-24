package basicapp.back.config.hibernate.type;

import basicapp.back.business.common.model.PostalCode;
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
