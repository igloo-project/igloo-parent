package fr.openwide.core.basicapp.core.config.hibernate.type;

import fr.openwide.core.basicapp.core.business.common.model.CodePostal;
import fr.openwide.core.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class CodePostalType extends AbstractImmutableMaterializedStringValueUserType<CodePostal> {

	@Override
	public Class<CodePostal> returnedClass() {
		return CodePostal.class;
	}

	@Override
	protected CodePostal instantiate(String value) {
		return new CodePostal(value);
	}
}
