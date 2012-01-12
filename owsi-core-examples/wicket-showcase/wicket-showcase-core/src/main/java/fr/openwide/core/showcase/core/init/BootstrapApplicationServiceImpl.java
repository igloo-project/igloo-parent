package fr.openwide.core.showcase.core.init;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.init.service.IImportDataService;

public class BootstrapApplicationServiceImpl {
	
	private static final String INIT_RESOURCE_PATH = "init";
	
	@Autowired
	private IImportDataService initDataService;
	
	@PostConstruct
	public void init() throws FileNotFoundException, ServiceException, SecurityServiceException, IOException {
		initDataService.importDirectory(new ClassPathResource(INIT_RESOURCE_PATH).getFile());
	}
}
