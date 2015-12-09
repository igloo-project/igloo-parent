package fr.openwide.core.test.jpa.more.business.parameter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.more.business.parameter.dao.IParameterDao;
import fr.openwide.core.jpa.more.business.parameter.service.AbstractParameterServiceImpl;

@Service("mockParameterService")
public class MockParameterServiceImpl extends AbstractParameterServiceImpl implements IMockParameterService {

	@Autowired
	public MockParameterServiceImpl(IParameterDao dao) {
		super(dao);
	}

}
