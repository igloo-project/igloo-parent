package fr.openwide.core.test.jpa.example.business.label.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.jpa.example.business.label.dao.LabelDao;
import fr.openwide.core.test.jpa.example.business.label.model.Label;

@Service("labelService")
public class LabelServiceImpl extends GenericEntityServiceImpl<String, Label>
		implements LabelService {

	@Autowired
	public LabelServiceImpl(LabelDao labelDao) {
		super(labelDao);
	}

}
