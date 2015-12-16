package fr.openwide.core.jpa.more.business.parameter.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;

@Repository("parameterDao")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Long, Parameter>
		implements IParameterDao, IMutablePropertyDao {

	@Override
	public String get(String key) {
		Parameter parameter = getByName(key);
		if (parameter == null) {
			return null;
		}
		return parameter.getStringValue();
	}

	@Override
	public void set(String key, String value) throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(key);
		if (parameter != null) {
			parameter.setStringValue(value);
			update(parameter);
		} else {
			save(new Parameter(key, value));
		}
	}

	@Override
	public Parameter getByName(String name) {
		return super.getByNaturalId(name);
	}

	@Override
	public void clean() {
		for (Parameter parameter : list()) {
			delete(parameter);
		}
	}

}
