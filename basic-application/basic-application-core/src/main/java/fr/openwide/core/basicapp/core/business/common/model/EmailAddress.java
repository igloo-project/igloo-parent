package fr.openwide.core.basicapp.core.business.common.model;

import fr.openwide.core.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;
import fr.openwide.core.spring.util.StringUtils;

public class EmailAddress extends AbstractMaterializedPrimitiveValue<String, EmailAddress> {

	private static final long serialVersionUID = -6091539685516602212L;

	public EmailAddress(String value) {
		super(StringUtils.lowerCase(value));
	}
}
