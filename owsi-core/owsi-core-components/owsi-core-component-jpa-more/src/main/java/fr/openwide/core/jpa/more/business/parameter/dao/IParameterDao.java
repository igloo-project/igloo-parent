package fr.openwide.core.jpa.more.business.parameter.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.spring.property.service.IPropertyService;

/**
 * @deprecated Parameters are deprecated in favor of properties.
 * @see IPropertyService
 */
@Deprecated
public interface IParameterDao extends IGenericEntityDao<Long, Parameter> {

	Parameter getByName(String name);

}