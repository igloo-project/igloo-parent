package basicapp.back.business.upgrade.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public abstract class AbstractDataUpgradeMigration extends BaseJavaMigration {

  @Value("${db.schema}")
  private String defaultSchema;

  @Override
  public void migrate(Context context) throws Exception {
    JdbcTemplate jdbcTemplate =
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

    for (Class<? extends IDataUpgrade> dataUpgradeClass : getDataUpgradeClasses()) {
      Integer id =
          jdbcTemplate.queryForObject(
              String.format(
                  "SELECT NEXTVAL('%s.%s_id_seq');",
                  defaultSchema, DataUpgradeRecord.class.getSimpleName()),
              Integer.class);

      jdbcTemplate.execute(
          String.format(
              "INSERT INTO %s.%s (id, name, autoPerform, done) VALUES (?, ?, ?, ?)",
              defaultSchema, DataUpgradeRecord.class.getSimpleName()),
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
