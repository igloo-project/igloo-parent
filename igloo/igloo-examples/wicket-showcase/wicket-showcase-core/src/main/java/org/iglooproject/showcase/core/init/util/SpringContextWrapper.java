package org.iglooproject.showcase.core.init.util;

import java.io.File;
import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.util.init.service.IImportDataService;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.util.EntityManagerUtils;

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
	
	public void importDirectory(File directory) throws ServiceException, SecurityServiceException, IOException {
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
