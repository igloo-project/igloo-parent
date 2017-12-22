package fr.openwide.core.basicapp.core.business.upgrade.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.schlichtherle.truezip.file.TFile;
import fr.openwide.core.basicapp.core.BasicApplicationCorePackage;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.jpa.more.util.init.service.IImportDataService;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.util.EntityManagerUtils;

public class ImportExcel implements IDataUpgrade {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportExcel.class);
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	@Autowired
	private IImportDataService importDataService;

	@Autowired
	private IHibernateSearchService hibernateSearchService;

	@Override
	public String getName() {
		return ImportExcel.class.getSimpleName();
	}

	@Override
	public void perform() throws ServiceException, SecurityServiceException {
		LOGGER.info("Performing MigrationTest upgrade");
		
		try {
			
			entityManagerUtils.openEntityManager();
			importDataService.importDirectory(new TFile( // May be inside a Jar
					BasicApplicationCorePackage.class.getResource("/init").toURI()
			));
			
			hibernateSearchService.reindexAll();
			LOGGER.info("Initialization complete");
		} catch (Throwable e) { // NOSONAR We just want to log the Exception/Error, no error handling here.
			LOGGER.error("Error during initialization", e);
			throw new IllegalStateException(e);
		}
	}
}
