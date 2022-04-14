package org.igloo.jpa.type;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

public class IglooBasicJpaTypeContributor implements TypeContributor {

	@Override
	public final void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		typeContributions.contributeType(new GenericEntityReferenceType());
	}

}
