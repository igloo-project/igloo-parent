package fr.openwide.core.jpa.more.business.parameter.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.jpa.more.business.parameter.model.QParameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Long, Parameter>
		implements IParameterDao {

	@Override
	public Parameter getByName(String name) {
		return super.getByField(QParameter.parameter, QParameter.parameter.name, name);
	}

}
