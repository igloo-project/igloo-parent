package fr.openwide.core.test.jpa.more.business.entity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.jpa.more.business.entity.dao.ITestEntityDao;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

@Service("testEntityService")
public class TestEntityServiceImpl extends GenericEntityServiceImpl<Long, TestEntity> implements ITestEntityService {

	@Autowired
	public TestEntityServiceImpl(ITestEntityDao testEntityDao) {
		super(testEntityDao);
	}
}
