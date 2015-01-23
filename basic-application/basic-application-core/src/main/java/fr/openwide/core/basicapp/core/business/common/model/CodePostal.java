package fr.openwide.core.basicapp.core.business.common.model;

import fr.openwide.core.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

public class CodePostal extends AbstractMaterializedPrimitiveValue<String, CodePostal> {

	private static final long serialVersionUID = -2876716982785775871L;

	public CodePostal(String value) {
		super(value);
	}
}
