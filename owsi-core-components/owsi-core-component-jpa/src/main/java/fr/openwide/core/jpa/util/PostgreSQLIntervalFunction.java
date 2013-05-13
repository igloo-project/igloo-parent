package fr.openwide.core.jpa.util;

import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

public class PostgreSQLIntervalFunction implements SQLFunction {

	@Override
	public boolean hasArguments() {
		return true;
	}

	@Override
	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	@Override
	public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory)
			throws QueryException {
		if (args.size() != 1) {
			throw new QueryException("interval() requires one argument");
		}
		
		return args.get(0) + "::interval";
	}

}