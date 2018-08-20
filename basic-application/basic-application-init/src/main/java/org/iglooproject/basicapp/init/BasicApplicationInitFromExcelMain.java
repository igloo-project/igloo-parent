package org.iglooproject.basicapp.init;

import java.io.IOException;

import org.iglooproject.basicapp.init.config.spring.BasicApplicationInitConfig;
import org.iglooproject.basicapp.init.util.SpringContextWrapper;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import de.schlichtherle.truezip.file.TFile;

public final class BasicApplicationInitFromExcelMain extends AbstractBasicApplicationMain {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicApplicationInitFromExcelMain.class);

	private BasicApplicationInitFromExcelMain() {
	}

	public static void main(String[] args) throws ServiceException, SecurityServiceException, IOException {
		new BasicApplicationInitFromExcelMain().run();
	}

	protected void run() {
		try (AnnotationConfigApplicationContext context = startContext("development", BasicApplicationInitConfig.class)) {
			SpringContextWrapper contextWrapper = context.getBean("springContextWrapper", SpringContextWrapper.class);
			
			contextWrapper.openEntityManager();
			contextWrapper.importDirectory(new TFile( // May be inside a Jar
					BasicApplicationInitFromExcelMain.class.getResource("/init").toURI()
			));
			
			contextWrapper.reindexAll();
			
			LOGGER.info("Initialization complete");
		} catch (Throwable e) { // NOSONAR We just want to log the Exception/Error, no error handling here.
			LOGGER.error("Error during initialization", e);
			throw new IllegalStateException(e);
		}
	}

}
