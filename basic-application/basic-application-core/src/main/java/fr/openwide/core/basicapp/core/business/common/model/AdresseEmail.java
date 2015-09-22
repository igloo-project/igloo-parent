package fr.openwide.core.basicapp.core.business.common.model;

import fr.openwide.core.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;
import fr.openwide.core.spring.util.StringUtils;

public class AdresseEmail extends AbstractMaterializedPrimitiveValue<String, AdresseEmail> {

	private static final long serialVersionUID = -6091539685516602212L;

	public AdresseEmail(String value) {
		super(StringUtils.lowerCase(value));
	}
}
