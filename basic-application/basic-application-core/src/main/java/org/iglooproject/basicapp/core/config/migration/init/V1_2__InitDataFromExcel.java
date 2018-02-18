package org.iglooproject.basicapp.core.config.migration.init;

import org.iglooproject.basicapp.core.business.upgrade.model.AbstractDataUpgradeMigration;
import org.iglooproject.basicapp.core.business.upgrade.model.DataUpgrade_InitDataFromExcel;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;

public class V1_2__InitDataFromExcel extends AbstractDataUpgradeMigration {

	@Override
	protected Class<? extends IDataUpgrade> getDataUpgradeClass() {
		return DataUpgrade_InitDataFromExcel.class;
	}
}
