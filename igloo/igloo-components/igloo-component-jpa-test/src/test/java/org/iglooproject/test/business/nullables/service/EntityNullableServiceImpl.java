package org.iglooproject.test.business.nullables.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.business.nullables.dao.IEntityNullableDao;
import org.iglooproject.test.business.nullables.model.EntityNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("entityNullableService")
public class EntityNullableServiceImpl extends GenericEntityServiceImpl<Long, EntityNullable>
    implements IEntityNullableService {

  @Autowired
  public EntityNullableServiceImpl(IEntityNullableDao entityNullableDao) {
    super(entityNullableDao);
  }
}
