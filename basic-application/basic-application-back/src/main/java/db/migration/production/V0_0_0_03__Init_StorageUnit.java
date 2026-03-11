package db.migration.production;

import basicapp.back.business.upgrade.model.AbstractUnitDataUpgradeMigration;
import basicapp.back.business.upgrade.model.DataUpgrade_InitStorageUnit;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.stereotype.Component;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
@Component
public class V0_0_0_03__Init_StorageUnit extends AbstractUnitDataUpgradeMigration {

  @Override
  protected Class<? extends IDataUpgrade> getDataUpgradeClass() {
    return DataUpgrade_InitStorageUnit.class;
  }
}
