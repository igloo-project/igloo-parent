package fr.openwide.core.test.jpa.example.business.label.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.jpa.example.business.label.model.Label;

@Repository("labelDao")
public class LabelDaoImpl extends GenericEntityDaoImpl<String, Label> implements
		LabelDao {

	public LabelDaoImpl() {
		super();
	}

}
