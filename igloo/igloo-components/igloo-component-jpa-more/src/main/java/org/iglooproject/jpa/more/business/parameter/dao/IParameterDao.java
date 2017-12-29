package org.iglooproject.jpa.more.business.parameter.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.more.business.parameter.model.Parameter;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyService;

/**
 * @deprecated Parameters are deprecated in favor of properties.
 * @see IPropertyService
 */
@Deprecated
public interface IParameterDao extends IGenericEntityDao<Long, Parameter>, IMutablePropertyDao {

	Parameter getByName(String name);

}