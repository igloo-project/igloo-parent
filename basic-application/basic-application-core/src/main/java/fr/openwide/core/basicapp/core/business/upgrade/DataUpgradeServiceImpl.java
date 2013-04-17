package fr.openwide.core.basicapp.core.business.upgrade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.jpa.more.business.upgrade.service.AbstractDataUpgradeServiceImpl;

@Service("dataUpgradeService")
public class DataUpgradeServiceImpl extends AbstractDataUpgradeServiceImpl {

	@Override
	public List<IDataUpgrade> listDataUpgrades() {
		return ImmutableList.<IDataUpgrade>of();
	}
}
