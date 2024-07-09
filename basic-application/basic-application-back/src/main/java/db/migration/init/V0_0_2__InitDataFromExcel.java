package db.migration.init;

import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.stereotype.Component;

import basicapp.back.business.upgrade.model.AbstractDataUpgradeMigration;
import basicapp.back.business.upgrade.model.DataUpgrade_InitDataFromExcel;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
@Component
public class V0_0_2__InitDataFromExcel extends AbstractDataUpgradeMigration {

	@Override
	protected Class<? extends IDataUpgrade> getDataUpgradeClass() {
		return DataUpgrade_InitDataFromExcel.class;
	}

}
