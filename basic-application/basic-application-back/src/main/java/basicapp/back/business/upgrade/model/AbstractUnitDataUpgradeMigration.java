package basicapp.back.business.upgrade.model;

import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;

public abstract class AbstractUnitDataUpgradeMigration extends AbstractDataUpgradeMigration {

  @Override
  protected Collection<Class<? extends IDataUpgrade>> getDataUpgradeClasses() {
    return List.of(getDataUpgradeClass());
  }

  protected abstract Class<? extends IDataUpgrade> getDataUpgradeClass();
}
