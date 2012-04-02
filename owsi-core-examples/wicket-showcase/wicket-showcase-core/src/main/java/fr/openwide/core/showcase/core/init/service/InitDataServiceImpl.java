package fr.openwide.core.showcase.core.init.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.util.init.service.AbstractImportDataServiceImpl;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserGroup;

@Service("initDataService")
public class InitDataServiceImpl extends AbstractImportDataServiceImpl {
	
	@Override
	protected List<String> getGenericListItemPackagesToScan() {
		return new ArrayList<String>();
	}
	
	@Override
	protected void importMainBusinessItems(Map<String, Map<Long, GenericEntity<Long, ?>>> idsMapping,
			Workbook workbook) {
		doImportItem(idsMapping, workbook, Authority.class);
		doImportItem(idsMapping, workbook, User.class);
		doImportItem(idsMapping, workbook, UserGroup.class);
	}
}
