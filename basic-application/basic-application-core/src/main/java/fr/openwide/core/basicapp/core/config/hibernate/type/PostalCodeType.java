package fr.openwide.core.basicapp.core.config.hibernate.type;

import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

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
