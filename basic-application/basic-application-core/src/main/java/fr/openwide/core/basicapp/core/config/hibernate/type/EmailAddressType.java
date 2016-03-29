package fr.openwide.core.basicapp.core.config.hibernate.type;

import fr.openwide.core.basicapp.core.business.common.model.EmailAddress;
import fr.openwide.core.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class EmailAddressType extends AbstractImmutableMaterializedStringValueUserType<EmailAddress> {

	@Override
	public Class<EmailAddress> returnedClass() {
		return EmailAddress.class;
	}

	@Override
	protected EmailAddress instantiate(String value) {
		return new EmailAddress(value);
	}
}
