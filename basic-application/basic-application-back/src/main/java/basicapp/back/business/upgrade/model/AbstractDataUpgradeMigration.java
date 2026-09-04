package basicapp.back.business.upgrade.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public abstract class AbstractDataUpgradeMigration extends BaseJavaMigration {

  @Override
  public void migrate(Context context) throws Exception {
    JdbcTemplate jdbcTemplate =
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

    for (Class<? extends IDataUpgrade> dataUpgradeClass : getDataUpgradeClasses()) {
      Integer id =
          jdbcTemplate.queryForObject("SELECT NEXTVAL('DataUpgradeRecord_id_seq');", Integer.class);

      jdbcTemplate.execute(
          "INSERT INTO DataUpgradeRecord (id, name, autoPerform, done) VALUES (?, ?, ?, ?)",
          new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException {
              ps.setInt(1, id);
              ps.setString(2, dataUpgradeClass.getSimpleName());
              ps.setBoolean(3, true);
              ps.setBoolean(4, false);
              return ps.execute();
            }
          });
    }
  }

  protected abstract Collection<Class<? extends IDataUpgrade>> getDataUpgradeClasses();

  @Override
  public Integer getChecksum() {
    return getDataUpgradeClasses().stream()
            .map(Class::getSimpleName)
            .collect(Collectors.joining("|"))
            .hashCode()
        * 23;
  }
}
