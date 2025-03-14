package basicapp.back.business.common.model;

import org.bindgen.Bindable;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

@Bindable
public class PostalCode extends AbstractMaterializedPrimitiveValue<String, PostalCode> {

  private static final long serialVersionUID = -2876716982785775871L;

  public PostalCode(String value) {
    super(value);
  }
}
