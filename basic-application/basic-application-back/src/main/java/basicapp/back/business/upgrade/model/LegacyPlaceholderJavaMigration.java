package basicapp.back.business.upgrade.model;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/// Placeholder class used to maintain an empty Java Flyway migration in the project.
/// This avoids having to delete the Java class, which would cause Flyway to fail with a missing
/// migration error.
public abstract class LegacyPlaceholderJavaMigration extends BaseJavaMigration {

  @Override
  public void migrate(Context context) throws Exception {
    // nothing to do
  }
}
