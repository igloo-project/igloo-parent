package org.igloo.hibernate.function;

import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

public class PostgreSQLRegexpOperatorFunction implements SQLFunction {

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
		return BooleanType.INSTANCE;
	}

	@Override
	public String render(Type firstArgumentType, @SuppressWarnings("rawtypes") List arguments, SessionFactoryImplementor factory)
			throws QueryException {
		if (arguments.size() != 2) {
			throw new IllegalStateException(String.format("2 arguments needed, %d found", arguments.size()));
		}
		return arguments.get(0) + " ~ " + arguments.get(1);
	}

}
