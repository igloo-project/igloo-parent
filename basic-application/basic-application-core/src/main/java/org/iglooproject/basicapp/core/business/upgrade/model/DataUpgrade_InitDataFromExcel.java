package org.iglooproject.basicapp.core.business.upgrade.model;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.jpa.more.util.init.service.IImportDataService;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
public class DataUpgrade_InitDataFromExcel implements IDataUpgrade {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgrade_InitDataFromExcel.class);

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	@Autowired
	private IImportDataService importDataService;

	@Autowired
	private IHibernateSearchService hibernateSearchService;

	@Override
	public String getName() {
		return DataUpgrade_InitDataFromExcel.class.getSimpleName();
	}

	@Override
	public void perform() throws ServiceException, SecurityServiceException {
		LOGGER.info("Performing MigrationTest upgrade");
		
		try {
			entityManagerUtils.openEntityManager();
			importDataService.importDirectory("init");
			
			hibernateSearchService.reindexAll();
			LOGGER.info("Initialization complete");
		} catch (Throwable e) { // NOSONAR We just want to log the Exception/Error, no error handling here.
			LOGGER.error("Error during initialization", e);
			throw new IllegalStateException(e);
		}
	}

}
