package org.igloo.hibernate.function;

import java.util.List;
import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Est utilisée pour permettre d'utiliser des intervals dans les requêtes QueryDSL.
 *
 * <p>Typiquement via :
 *
 * <pre>
 * query.from(QUser.user).where(BooleanTemplate.create("{0} - {2} * interval({1}) < now()", QUser.user.creationDate, "1 day", 4));
 * </pre>
 */
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
