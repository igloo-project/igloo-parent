package basicapp.back.business.common.model;

import org.bindgen.Bindable;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;
import org.iglooproject.spring.util.StringUtils;

@Bindable
public class EmailAddress extends AbstractMaterializedPrimitiveValue<String, EmailAddress> {

  private static final long serialVersionUID = 1L;

  public EmailAddress(String value) {
    super(StringUtils.lowerCase(value));
  }
}
