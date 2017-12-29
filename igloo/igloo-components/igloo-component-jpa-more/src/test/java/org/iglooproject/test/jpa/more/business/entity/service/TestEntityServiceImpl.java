package org.iglooproject.test.jpa.more.business.entity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.jpa.more.business.entity.dao.ITestEntityDao;
import org.iglooproject.test.jpa.more.business.entity.model.TestEntity;

@Service("testEntityService")
public class TestEntityServiceImpl extends GenericEntityServiceImpl<Long, TestEntity> implements ITestEntityService {

	@Autowired
	public TestEntityServiceImpl(ITestEntityDao testEntityDao) {
		super(testEntityDao);
	}
}
