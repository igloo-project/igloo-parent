package org.iglooproject.test.business.nullables.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.nullables.model.EntityNullable;
import org.springframework.stereotype.Repository;

@Repository("entityNullableDao")
public class EntityNullableDaoImpl extends GenericEntityDaoImpl<Long, EntityNullable>
    implements IEntityNullableDao {}
