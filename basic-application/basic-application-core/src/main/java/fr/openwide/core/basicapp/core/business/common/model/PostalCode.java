package fr.openwide.core.basicapp.core.business.common.model;

import fr.openwide.core.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

public class PostalCode extends AbstractMaterializedPrimitiveValue<String, PostalCode> {

	private static final long serialVersionUID = -2876716982785775871L;

	public PostalCode(String value) {
		super(value);
	}
}
