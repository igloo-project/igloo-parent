package fr.openwide.core.jpa.more.business.property.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.dao.IParameterDao;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;

@Repository("mutablePropertyDao")
public class MutablePropertyDao implements IMutablePropertyDao {

	@Autowired
	private IParameterDao parameterDao;

	@Override
	public String get(String key) {
		Parameter parameter = parameterDao.getByName(key);
		if (parameter == null) {
			return null;
		}
		return parameter.getStringValue();
	}

	@Override
	public void set(String key, String value) throws ServiceException, SecurityServiceException {
		Parameter parameter = parameterDao.getByName(key);
		if (parameter != null) {
			parameter.setStringValue(value);
			parameterDao.update(parameter);
		} else {
			parameterDao.save(new Parameter(key, value));
		}
	}

}
