package fr.openwide.core.basicapp.core.config.hibernate.type;

import fr.openwide.core.basicapp.core.business.common.model.AdresseEmail;
import fr.openwide.core.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;

public class AdresseEmailType extends AbstractImmutableMaterializedStringValueUserType<AdresseEmail> {

	@Override
	public Class<AdresseEmail> returnedClass() {
		return AdresseEmail.class;
	}

	@Override
	protected AdresseEmail instantiate(String value) {
		return new AdresseEmail(value);
	}
}
