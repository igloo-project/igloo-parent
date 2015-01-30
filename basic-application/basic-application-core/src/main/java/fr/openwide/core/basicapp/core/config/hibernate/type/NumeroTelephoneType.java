package fr.openwide.core.basicapp.core.config.hibernate.type;

import fr.openwide.core.basicapp.core.business.common.model.NumeroTelephone;
import fr.openwide.core.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class NumeroTelephoneType extends AbstractImmutableMaterializedStringValueUserType<NumeroTelephone> {

	@Override
	public Class<NumeroTelephone> returnedClass() {
		return NumeroTelephone.class;
	}

	@Override
	protected NumeroTelephone instantiate(String value) {
		return NumeroTelephone.buildClean(value);
	}
}
