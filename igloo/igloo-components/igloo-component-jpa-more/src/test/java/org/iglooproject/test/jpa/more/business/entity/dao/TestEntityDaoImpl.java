package org.iglooproject.test.jpa.more.business.entity.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.jpa.more.business.entity.model.TestEntity;

@Repository("testEntityDao")
public class TestEntityDaoImpl extends GenericEntityDaoImpl<Long, TestEntity> implements ITestEntityDao {

}
