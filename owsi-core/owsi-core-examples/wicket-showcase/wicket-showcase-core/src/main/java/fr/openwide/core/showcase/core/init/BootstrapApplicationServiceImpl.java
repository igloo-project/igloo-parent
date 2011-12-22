package fr.openwide.core.showcase.core.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.init.service.IImportDataService;

public class BootstrapApplicationServiceImpl {
	
	private static final String INIT_RESSOURCE_PATH = "src/main/resources/init";
	
	@Autowired
	private IImportDataService initDataService;

	@PostConstruct
	public void init() throws FileNotFoundException, ServiceException, SecurityServiceException, IOException {
		initDataService.importDirectory(new File("/home/jgonzalez/Documents/OWSI-Core/workspace/wicket-showcase/wicket-showcase-core/" + INIT_RESSOURCE_PATH));
	}
}
