package org.iglooproject.test.business.label.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.business.label.dao.ILabelDao;
import org.iglooproject.test.business.label.model.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("labelService")
public class LabelServiceImpl extends GenericEntityServiceImpl<String, Label>
		implements ILabelService {

	@Autowired
	public LabelServiceImpl(ILabelDao labelDao) {
		super(labelDao);
	}

}
