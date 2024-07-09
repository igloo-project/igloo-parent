package basicapp.back.business.common.model;

import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;
import org.iglooproject.spring.util.StringUtils;

public class EmailAddress extends AbstractMaterializedPrimitiveValue<String, EmailAddress> {

	private static final long serialVersionUID = -6091539685516602212L;

	public EmailAddress(String value) {
		super(StringUtils.lowerCase(value));
	}

}
