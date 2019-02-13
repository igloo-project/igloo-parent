package test.jpa.more.business.entity.dao;

import org.springframework.stereotype.Repository;

import test.jpa.more.business.entity.model.TestEntity;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("testEntityDao")
public class TestEntityDaoImpl extends GenericEntityDaoImpl<Long, TestEntity> implements ITestEntityDao {

}
