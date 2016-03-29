package fr.openwide.core.basicapp.core.config.hibernate.type;

import fr.openwide.core.basicapp.core.business.common.model.PhoneNumber;
import fr.openwide.core.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class PhoneNumberType extends AbstractImmutableMaterializedStringValueUserType<PhoneNumber> {

	@Override
	public Class<PhoneNumber> returnedClass() {
		return PhoneNumber.class;
	}

	@Override
	protected PhoneNumber instantiate(String value) {
		return PhoneNumber.buildClean(value);
	}
}
