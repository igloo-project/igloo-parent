package fr.openwide.core.jpa.business.generic.query;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityCollectionReference;
import fr.openwide.core.jpa.query.IQuery;

public interface IEntityReferenceQuery<T extends GenericEntity<?, ?>> extends IQuery<T> {

	EntityReferenceQueryImpl<T> setReference(GenericEntityCollectionReference<?, T> reference);

}