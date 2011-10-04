package fr.openwide.core.jpa.more.business.parameter.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Integer, Parameter>
		implements IParameterDao {

}
