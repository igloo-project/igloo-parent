package fr.openwide.core.test.jpa.more.business.entity.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

@Repository("testEntityDao")
public class TestEntityDaoImpl extends GenericEntityDaoImpl<Long, TestEntity> implements ITestEntityDao {

}
