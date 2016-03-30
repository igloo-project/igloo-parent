package fr.openwide.core.test.business.label.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.business.label.model.Label;

@Repository("labelDao")
public class LabelDaoImpl extends GenericEntityDaoImpl<String, Label> implements
		ILabelDao {

	public LabelDaoImpl() {
		super();
	}

}
