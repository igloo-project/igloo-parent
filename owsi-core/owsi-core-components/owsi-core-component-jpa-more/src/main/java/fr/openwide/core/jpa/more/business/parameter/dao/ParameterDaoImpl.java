package fr.openwide.core.jpa.more.business.parameter.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends GenericEntityDaoImpl<Long, Parameter>
		implements IParameterDao {

	@Override
	public <T extends Parameter, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}

}
