package fr.openwide.core.showcase.core.init.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.init.service.IImportDataService;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.util.EntityManagerUtils;

@Component
public class SpringContextWrapper {

	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private IImportDataService importDataService;
	
	public void importDirectory(File directory) throws FileNotFoundException, ServiceException, SecurityServiceException, IOException {
		importDataService.importDirectory(directory);
		// la re-indexation doit être effectuée hors scope transaction
		reindexAll();
	}
	
	public void reindexAll() throws ServiceException {
		hibernateSearchService.reindexAll();
	}
	
	public void openEntityManager() {
		entityManagerUtils.openEntityManager();
	}
	
	public void closeEntityManager() {
		entityManagerUtils.closeEntityManager();
		entityManagerFactory.close();
	}
}
