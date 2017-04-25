package fr.openwide.core.basicapp.core.business.upgrade.dao;

import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_PREFIX;
import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_SUFFIX;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.jpa.more.business.parameter.model.QParameter;

@Repository("dataUpgradeDao")
public class DataUpgradeDaoImpl extends JpaDaoSupport implements IDataUpgradeDao {
	
	private static final QParameter qParameter = QParameter.parameter;

	public List<String> listDataUpgradeAutoPerfom() {
		List<Parameter> listParameter = new JPAQuery<>(getEntityManager())
				.select(qParameter).distinct()
				.from(qParameter)
				.where(qParameter.name.like(DATA_UPGRADE_AUTOPERFOM_PREFIX + ".%." + DATA_UPGRADE_AUTOPERFOM_SUFFIX),
						qParameter.stringValue.eq("true"),
						qParameter.dateValue.isNotNull())
				.orderBy(qParameter.dateValue.desc())
				.fetch();
		
		List<String> listUpgrades = Lists.transform(listParameter, new Function<Parameter, String>() {
			@Override
			public String apply(Parameter input) {
				return input.getName();
			}
		});
		
		return listUpgrades;
	}
}
