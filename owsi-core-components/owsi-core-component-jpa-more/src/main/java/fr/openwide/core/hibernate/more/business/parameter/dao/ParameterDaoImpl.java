package fr.openwide.core.hibernate.more.business.parameter.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.hibernate.more.business.parameter.model.Parameter;
import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("parameterDao")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Integer, Parameter>
		implements ParameterDao {

}
