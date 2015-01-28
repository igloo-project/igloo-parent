package fr.openwide.core.jpa.more.business.parameter.dao;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;

public interface IParameterDao extends IGenericEntityDao<Long, Parameter> {

	<T extends Parameter, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue);

}