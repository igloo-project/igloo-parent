package fr.openwide.core.hibernate.more.business.parameter.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.hibernate.more.business.parameter.model.Parameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Integer, Parameter>
		implements ParameterDao {

	@Autowired
	public ParameterDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
