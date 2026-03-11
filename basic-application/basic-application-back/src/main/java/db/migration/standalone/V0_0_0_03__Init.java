package db.migration.standalone;

import basicapp.back.business.upgrade.model.AbstractDataUpgradeMigration;
import basicapp.back.business.upgrade.model.DataUpgrade_InitDataExcel;
import basicapp.back.business.upgrade.model.DataUpgrade_InitStorageUnit;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.stereotype.Component;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
@Component
public class V0_0_0_03__Init extends AbstractDataUpgradeMigration {

  @Override
  protected Collection<Class<? extends IDataUpgrade>> getDataUpgradeClasses() {
    return List.of(DataUpgrade_InitDataExcel.class, DataUpgrade_InitStorageUnit.class);
  }
}
