package org.iglooproject.showcase.core.init;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.util.init.service.IImportDataService;
import org.iglooproject.jpa.search.service.IHibernateSearchService;

public class BootstrapApplicationServiceImpl {
	
	private static final String INIT_RESOURCE_PATH = "init";
	
	@Autowired
	private IImportDataService initDataService;
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@PostConstruct
	public void init() throws FileNotFoundException, ServiceException, SecurityServiceException, IOException {
		initDataService.importDirectory(new ClassPathResource(INIT_RESOURCE_PATH).getFile());
		hibernateSearchService.reindexAll();
	}
}
