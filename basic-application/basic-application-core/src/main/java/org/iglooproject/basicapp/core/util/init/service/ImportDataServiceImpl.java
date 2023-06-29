package org.iglooproject.basicapp.core.util.init.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.iglooproject.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.util.init.service.AbstractImportDataServiceImpl;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ImportDataServiceImpl extends AbstractImportDataServiceImpl {

	@Override
	protected List<String> getReferenceDataPackagesToScan() {
		return Lists.newArrayList(BasicApplicationCoreCommonBusinessPackage.class.getPackage().getName());
	}

	@Override
	protected void importMainBusinessItems(Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping, Workbook workbook) {
		doImportItem(idsMapping, workbook, Authority.class);
		doImportItem(idsMapping, workbook, UserGroup.class);
		doImportItem(idsMapping, workbook, TechnicalUser.class);
		doImportItem(idsMapping, workbook, BasicUser.class);
	}

	@Override
	protected void customizeConversionService(GenericConversionService conversionService) {
		// no customization
	}

}