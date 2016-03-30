package fr.openwide.core.test.business.label.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.business.label.dao.ILabelDao;
import fr.openwide.core.test.business.label.model.Label;

@Service("labelService")
public class LabelServiceImpl extends GenericEntityServiceImpl<String, Label>
		implements ILabelService {

	@Autowired
	public LabelServiceImpl(ILabelDao labelDao) {
		super(labelDao);
	}

}
