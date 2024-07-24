package org.iglooproject.jpa.business.generic.query;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.query.IQuery;

public interface IEntityReferenceQuery<T extends GenericEntity<?, ?>> extends IQuery<T> {

  EntityReferenceQueryImpl<T> setReference(GenericEntityCollectionReference<?, T> reference);
}
