package igloo.test.listener;

import igloo.test.listener.model.IglooTestListenerType;
import org.springframework.test.context.jdbc.Sql;

/** This listener triggers after @{@link Sql}. */
public class AfterSqlIglooTestExecutionListener extends IglooTestExecutionListener {
  public AfterSqlIglooTestExecutionListener() {
    super(5000 + 100, "after-sql", IglooTestListenerType.AUTO);
  }
}
