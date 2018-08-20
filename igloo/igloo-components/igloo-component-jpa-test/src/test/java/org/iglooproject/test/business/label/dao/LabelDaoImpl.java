package org.iglooproject.test.business.label.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.label.model.Label;

@Repository("labelDao")
public class LabelDaoImpl extends GenericEntityDaoImpl<String, Label> implements
		ILabelDao {

	public LabelDaoImpl() {
		super();
	}

}
